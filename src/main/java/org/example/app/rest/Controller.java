package org.example.app.rest;

import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.example.app.service.kafka.producer.KafkaReactiveProducer;
import org.example.app.service.postgres.InscritosService;
import org.example.domain.dto.InscritoDTO;
import org.example.domain.dto.PaginatedResponse;
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
    KafkaReactiveProducer producer;

    @Autowired
    private InscritosService service;

    @PostMapping("/start")
    public Mono<Void> start(@RequestBody InscritoDTO inscritoDTO) throws IOException {
        log.info("Iniciando a aplicação.");
        return producer.send(inscritoDTO);
    }

    @GetMapping("/{id}")
    public Mono<InscritoEntity> findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @GetMapping
    public Flux<InscritoEntity> findAll() {
        return service.findAll();
    }

    @GetMapping("/paginado")
    public Mono<PaginatedResponse<InscritoEntity>> findAllPeageble(@RequestParam(defaultValue = "0") int page,
                                                                   @RequestParam(defaultValue = "10") int size,
                                                                   String orderBy,
                                                                   String direction) {
        return service.findAllPeageble(page, size, orderBy, direction);
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