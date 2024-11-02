package org.example.app.service.kafka.consumer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.example.app.service.postgres.InscritosService;
import org.example.domain.dto.InscritoDTO;
import org.example.infra.mapper.InscritoMapper;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.kafka.receiver.KafkaReceiver;

@Service
@Slf4j

public class KafkaReactiveConsumer {

    private final KafkaReceiver<String, String> kafkaReceiver;
    private final InscritosService service;
    private final ObjectMapper objectMapper;
    private final InscritoMapper inscritoMapper;

    public KafkaReactiveConsumer(KafkaReceiver<String, String> kafkaReceiver, InscritosService service, ObjectMapper objectMapper, InscritoMapper inscritoMapper) {
        this.kafkaReceiver = kafkaReceiver;
        this.service = service;
        this.objectMapper = objectMapper;
        this.inscritoMapper = inscritoMapper;
    }

    @EventListener(ContextRefreshedEvent.class)
    public void startConsuming() {
        kafkaReceiver.receive()
                .flatMap(record -> {
                    String message = record.value();
                    try {
                        log.info("Tópico consumido com sucesso no Kafka...");
                        InscritoDTO inscritoDTO = objectMapper.readValue(message, new TypeReference<>() {});
                        log.info("Mapper realizado com sucesso");

                        return service.save(inscritoMapper.toEntity(inscritoDTO))
                                .doOnSuccess(entity -> {
                                    log.info("Inscrito salvo com sucesso: {}", inscritoMapper.toDTO(entity).toString());
                                    record.receiverOffset().acknowledge();
                                })
                                .doOnError(error -> log.error("Erro ao salvar no banco de dados: ", error));

                    } catch (Exception e) {
                        log.error("Erro ao processar a mensagem: ", e);
                        return Mono.empty();
                    }
                }).subscribe();
    }

}
