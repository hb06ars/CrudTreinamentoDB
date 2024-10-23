package org.example.infra.repository.postgres.impl;

import org.example.domain.entity.InscritoEntity;
import org.example.infra.repository.postgres.CustomInscritoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
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

    @Override
    public Flux<InscritoEntity> findAllPeageble(int page, int size, String orderBy, String direction) {
        direction = !"ASC".equalsIgnoreCase(direction) &&
                    !"DESC".equalsIgnoreCase(direction) ? "ASC" : direction;

        String query =  "SELECT * FROM public.inscrito " +
                        "ORDER BY " + orderBy + " " + direction + " " +
                        "LIMIT :size OFFSET :offset";

        return databaseClient.sql(query)
                .bind("size", size)
                .bind("offset", page * size)
                .map((row, rowMetadata) -> InscritoEntity.builder()
                        .id(row.get("id", Long.class))
                        .nome(row.get("nome", String.class))
                        .telefone(row.get("telefone", String.class))
                        .build()
                ).all();
    }
}
