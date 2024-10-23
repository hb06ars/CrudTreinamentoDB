package org.example.infra.repository.postgres;

import org.example.domain.entity.InscritoEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CustomInscritoRepository {
    Mono<Long> findNextSequenceId();

    Flux<InscritoEntity> findAllPeageble(int page, int size, String orderBy, String direction);

}