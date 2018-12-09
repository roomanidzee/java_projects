package com.romanidze.reactivchina.config.psql;

import io.r2dbc.postgresql.PostgresqlConnectionConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.function.TransactionalDatabaseClient;

/**
 * 08.12.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Configuration
public class DatabaseConfig {

    private final DatabaseProperties databaseProperties;

    @Autowired
    public DatabaseConfig(DatabaseProperties databaseProperties) {
        this.databaseProperties = databaseProperties;
    }

    @Bean
    public PostgresqlConnectionFactory postgresqlConnectionFactory(){

        PostgresqlConnectionConfiguration config =
                PostgresqlConnectionConfiguration.builder()
                                                 .host(this.databaseProperties.getHost())
                                                 .port(this.databaseProperties.getPort())
                                                 .database(this.databaseProperties.getDatabase())
                                                 .username(this.databaseProperties.getUsername())
                                                 .password(this.databaseProperties.getPassword())
                                                 .build();

        return new PostgresqlConnectionFactory(config);

    }

    @Bean
    public TransactionalDatabaseClient databaseClient(PostgresqlConnectionFactory connectionFactory){
        return TransactionalDatabaseClient.create(connectionFactory);
    }

}
