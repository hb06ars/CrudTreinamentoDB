package org.example.infra.config.postgre;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.r2dbc.core.DatabaseClient;
import reactor.core.publisher.Mono;

@Configuration
@Slf4j
public class PostgreSQLCreateSequence {

    private final DatabaseClient databaseClient;

    public PostgreSQLCreateSequence(DatabaseClient databaseClient) {
        this.databaseClient = databaseClient;
    }

    @Bean
    public Mono<Void> createSequence() {
        return databaseClient.sql("CREATE SEQUENCE IF NOT EXISTS inscrito_id_seq START 1")
                .fetch()
                .rowsUpdated()
                .then();
    }

}
