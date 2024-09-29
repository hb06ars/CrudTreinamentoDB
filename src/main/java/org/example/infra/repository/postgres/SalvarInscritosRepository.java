package org.example.infra.repository.postgres;

import org.example.domain.entity.InscritoEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;


public interface SalvarInscritosRepository extends ReactiveCrudRepository<InscritoEntity, Long> {
}