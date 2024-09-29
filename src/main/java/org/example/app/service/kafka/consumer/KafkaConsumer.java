package org.example.app.service.kafka.consumer;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.example.app.service.kafka.postgres.SalvarInscritosService;
import org.example.domain.dto.InscritoDTO;
import org.example.infra.mapper.InscritoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class KafkaConsumer {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private InscritoMapper inscritoMapper;

    @Autowired
    private SalvarInscritosService service;

    @KafkaListener(topics = "${spring.kafka.topic}")
    public void execute(@Payload String message) throws JsonProcessingException {
        log.info("Tópico consumido com sucesso no Kafka...");
        InscritoDTO inscritoDTO = objectMapper.readValue(message, new TypeReference<>() {});
        log.info("Mapper realizado com sucesso");
        salvar(inscritoDTO);
    }

    private void salvar(InscritoDTO inscritoDTO) {
        service.save(inscritoMapper.toEntity(inscritoDTO))
            .doOnSuccess(inscritoEntity -> {
                log.info("Salvo com sucesso: {}", inscritoMapper.toDTO(inscritoEntity).toString());
            })
            .doOnError(error -> log.error("Erro ao salvar no Postgre: {}", error.getMessage()))
            .subscribe();
    }
}
