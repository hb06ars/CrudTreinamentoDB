package org.example.app.rest;

import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.example.app.service.kafka.postgres.SalvarInscritosService;
import org.example.app.service.kafka.producer.KafkaProducer;
import org.example.domain.dto.InscritoDTO;
import org.example.domain.entity.InscritoEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/")
@Slf4j
public class Controller {

    @Autowired
    KafkaProducer producer;

    @Autowired
    private SalvarInscritosService service;

    @PostMapping("/start")
    public void start(@RequestBody InscritoDTO inscritoDTO) throws IOException {
        log.info("Iniciando a aplicação.");
        producer.send(inscritoDTO);
        log.info("Aplicação finalizada.");
    }

    @GetMapping("/{id}")
    public Mono<InscritoEntity> findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @GetMapping
    public Flux<InscritoEntity> findAll() {
        return service.findAll();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<InscritoEntity> save(@RequestBody InscritoEntity inscritoEntity) {
        return service.save(inscritoEntity);
    }

    @PutMapping("/{id}")
    public Mono<InscritoEntity> update(@PathVariable Long id, @RequestBody InscritoEntity inscritoEntity) {
        return service.update(id, inscritoEntity);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteById(@PathVariable Long id) {
        return service.deleteById(id);
    }


}