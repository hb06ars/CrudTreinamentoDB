package org.example.app.service.kafka.producer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.example.domain.dto.InscritoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
public class KafkaProducer {

    @Value("${spring.kafka.topic}")
    private String nomeDoTopico;

    @Autowired
    private ObjectMapper objectMapper;

    private final KafkaTemplate<String, String> kafkaTemplate;

    public KafkaProducer(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    public void send(InscritoDTO inscritoDTO) throws JsonProcessingException {
        log.info("Testando o producer do Kafka...");
        this.kafkaTemplate.send(nomeDoTopico, UUID.randomUUID().toString(), objectMapper.writeValueAsString(inscritoDTO));
        log.info("\nTeste do producer do Kafka finalizado.");
        // Será consumido em >> package org.example.app.service.kafka.consumer.KafkaConsumer
    }
}
