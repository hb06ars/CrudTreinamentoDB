package org.example.app.service.kafka.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.example.domain.dto.InscritoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderRecord;

import java.util.UUID;

@Service
@Slf4j
public class KafkaReactiveProducer {

    private final KafkaSender<String, String> sender;
    private final ObjectMapper objectMapper;
    private final String topic;

    @Autowired
    public KafkaReactiveProducer(KafkaSender<String, String> sender,
                                 ObjectMapper objectMapper,
                                 @Value("${spring.kafka.topic}") String topic) {
        this.sender = sender;
        this.objectMapper = objectMapper;
        this.topic = topic;
    }

    public Mono<Void> send(InscritoDTO inscritoDTO) {
        return Mono.fromCallable(() -> {
                    String payload = objectMapper.writeValueAsString(inscritoDTO);
                    return SenderRecord.create(topic, null, null, UUID.randomUUID().toString(), payload, 1);
                })
                .flatMap(record ->
                        sender.send(Mono.just(record))
                                .doOnError(e -> log.error("Falha no envio: {}", e.getMessage()))
                                .doOnNext(r -> log.info("Mensagem {} enviada com sucesso", r.correlationMetadata()))
                                .then()
                )
                .onErrorResume(JsonProcessingException.class, e -> {
                    log.error("Erro ao processar JSON: {}", e.getMessage());
                    return Mono.empty();
                });
    }
}
