package org.example.app.service.kafka.consumer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.example.app.service.postgres.SalvarInscritosService;
import org.example.domain.dto.InscritoDTO;
import org.example.infra.mapper.InscritoMapper;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.kafka.receiver.KafkaReceiver;
import reactor.kafka.receiver.ReceiverOptions;
import reactor.core.publisher.Flux;

@Service
@Slf4j
public class KafkaReactiveConsumer {

    private final KafkaReceiver<String, String> kafkaReceiver;
    private final ObjectMapper objectMapper;
    private final SalvarInscritosService service;
    private final InscritoMapper inscritoMapper;

    public KafkaReactiveConsumer(ReceiverOptions<String, String> receiverOptions,
                                 ObjectMapper objectMapper,
                                 SalvarInscritosService service,
                                 InscritoMapper inscritoMapper) {
        this.kafkaReceiver = KafkaReceiver.create(receiverOptions);
        this.objectMapper = objectMapper;
        this.service = service;
        this.inscritoMapper = inscritoMapper;

        // Recebendo mensagens do Kafka
        Flux<?> kafkaFlux = kafkaReceiver.receive()
                .flatMap(record -> {
                    String message = record.value();
                    try {
                        log.info("Tópico consumido com sucesso no Kafka...");

                        // Mapeando a mensagem para o DTO InscritoDTO
                        InscritoDTO inscritoDTO = objectMapper.readValue(message, new TypeReference<>() {});
                        log.info("Mapper realizado com sucesso");

                        // Salvando o DTO processado e retornando o Mono de persistência
                        return service.save(inscritoMapper.toEntity(inscritoDTO))
                                .doOnSuccess(entity -> {
                                    log.info("Entidade salva com sucesso: {}", inscritoMapper.toDTO(entity).toString());
                                    // Confirmar processamento da mensagem APÓS o sucesso do salvamento
                                    record.receiverOffset().acknowledge();
                                })
                                .doOnError(error -> log.error("Erro ao salvar no banco de dados: ", error));

                    } catch (Exception e) {
                        log.error("Erro ao processar a mensagem: ", e);
                        return Mono.empty();  // Retorna vazio em caso de erro
                    }
                });

        kafkaFlux.subscribe();
    }
}
