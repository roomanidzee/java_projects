package com.romanidze.siteinfo.config;

import com.romanidze.siteinfo.repositories.MeasurementPostgreSQLRepository;
import io.r2dbc.postgresql.PostgresqlConnectionConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.r2dbc.function.DatabaseClient;
import org.springframework.data.r2dbc.repository.support.R2dbcRepositoryFactory;
import org.springframework.data.relational.core.mapping.RelationalMappingContext;

/**
 * 03.11.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Configuration
public class PostgreSQLConfig {

    @Value("${database.host}")
    private String databaseHost;

    @Value("${database.port}")
    private int databasePort;

    @Value("${database.title}")
    private String databaseTitle;

    @Value("${database.username}")
    private String databaseUsername;

    @Value("${database.password}")
    private String databasePassword;

    @Bean
    public PostgresqlConnectionFactory postgresqlConnectionFactory(){

        PostgresqlConnectionConfiguration config =
                PostgresqlConnectionConfiguration.builder()
                                                 .host(this.databaseHost)
                                                 .port(this.databasePort)
                                                 .database(this.databaseTitle)
                                                 .username(this.databaseUsername)
                                                 .password(this.databasePassword)
                                                 .build();

        return new PostgresqlConnectionFactory(config);

    }

    @Bean
    public DatabaseClient databaseClient(PostgresqlConnectionFactory connectionFactory){
        return DatabaseClient.create(connectionFactory);
    }

    @Bean
    public R2dbcRepositoryFactory factory(DatabaseClient client) {
        RelationalMappingContext context = new RelationalMappingContext();
        context.afterPropertiesSet();
        return new R2dbcRepositoryFactory(client, context);
    }

    @Bean
    public MeasurementPostgreSQLRepository repository(R2dbcRepositoryFactory factory){
        return factory.getRepository(MeasurementPostgreSQLRepository.class);
    }

}
