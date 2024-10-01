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

    public void send(InscritoDTO inscritoDTO) {
        try {
            SenderRecord<String, String, Integer> record = SenderRecord.create(topic, null, null,
                    UUID.randomUUID().toString(), objectMapper.writeValueAsString(inscritoDTO), 1);

            sender.send(Mono.just(record))
                    .doOnError(e -> System.err.println("Falha no envio: " + e))
                    .doOnNext(r -> System.out.println("Mensagem " + r.correlationMetadata() + " sent successfully"))
                    .subscribe();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}