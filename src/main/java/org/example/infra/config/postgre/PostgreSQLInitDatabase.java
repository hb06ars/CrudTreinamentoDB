package org.example.infra.config.postgre;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class PostgreSQLInitDatabase {

    @Autowired
    private DatabaseClient databaseClient;

    @PostConstruct
    public void init() {
        databaseClient.sql("CREATE SEQUENCE IF NOT EXISTS inscrito_id_seq START 1").fetch().rowsUpdated().block();
        databaseClient.sql("CREATE TABLE IF NOT EXISTS inscrito (id BIGINT PRIMARY KEY DEFAULT nextval('inscrito_id_seq'), nome VARCHAR(255), telefone VARCHAR(255))")
                .fetch().rowsUpdated().block();
    }
}
