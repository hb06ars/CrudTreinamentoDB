package org.example.infra.repository.postgres;

import reactor.core.publisher.Mono;

public interface CustomInscritoRepository {
    Mono<Long> findNextSequenceId();
}