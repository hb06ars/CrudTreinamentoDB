package org.example.infra.repository.postgres.impl;

import org.example.infra.repository.postgres.CustomInscritoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public class CustomInscritoRepositoryImpl implements CustomInscritoRepository {

    @Autowired
    private DatabaseClient databaseClient;

    @Override
    public Mono<Long> findNextSequenceId() {
        return databaseClient.sql("SELECT nextval('inscrito_id_seq') AS id")
                .map(row -> row.get("id", Long.class))
                .one();
    }
}
