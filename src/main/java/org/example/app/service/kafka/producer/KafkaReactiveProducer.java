package org.example.app.service.kafka.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.domain.dto.InscritoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.kafka.sender.KafkaSender;
import reactor.kafka.sender.SenderRecord;

import java.util.UUID;

@Service
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
                                .doOnError(e -> System.err.println("Falha no envio: " + e))
                                .doOnNext(r -> System.out.println("Mensagem " + r.correlationMetadata() + " enviada com sucesso"))
                                .then()
                )
                .onErrorResume(JsonProcessingException.class, e -> {
                    System.err.println("Erro ao processar JSON: " + e);
                    return Mono.empty();
                });
    }
}
