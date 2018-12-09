package com.romanidze.reactivchina.repositories.implementations;

import com.romanidze.reactivchina.domain.User;
import com.romanidze.reactivchina.repositories.interfaces.UserRepository;

import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.r2dbc.function.TransactionalDatabaseClient;
import org.springframework.stereotype.Repository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.BiFunction;

/**
 * 08.12.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Repository
public class UserRepositoryImpl implements UserRepository {

    private static final String INSERT_QUERY =
            "INSERT INTO person (username, password) VALUES($1, $2)";

    private static final String SELECT_QUERY =
            "SELECT * FROM person WHERE id = $1";

    private static final String SELECT_ALL_QUERY =
            "SELECT * FROM person";

    private static final String UPDATE_QUERY =
            "UPDATE person SET username = $2, password = $3 WHERE id = $1";

    private static final String DELETE_QUERY =
            "DELETE FROM person WHERE id = $1";

    private final TransactionalDatabaseClient databaseClient;

    private BiFunction<Row, RowMetadata, User> rowMapper =
            (row, metadata ) -> User.builder()
                                    .id(row.get(0, Integer.class).longValue())
                                    .username(row.get(1, String.class))
                                    .password(row.get(2, String.class))
                                    .build();

    @Autowired
    public UserRepositoryImpl(TransactionalDatabaseClient databaseClient) {
        this.databaseClient = databaseClient;
    }

    @Override
    public Flux<User> findAll() {
        return this.databaseClient.execute()
                                  .sql(SELECT_ALL_QUERY)
                                  .map(this.rowMapper)
                                  .all();
    }

    @Override
    public Mono<User> find(Long id) {

        //да, тут костыль, но в официальной либе это пока не исправлено

        return this.databaseClient.execute()
                                  .sql(SELECT_QUERY)
                                  .bind("$1", id)
                                  .fetch()
                                  .one()
                                  .map(row -> User.builder()
                                                  .id(((Integer) row.get("id")).longValue())
                                                  .username((String) row.get("username"))
                                                  .password((String) row.get("password"))
                                                  .build());

    }

    @Override
    public Flux<Integer> save(User model) {
        return this.databaseClient.inTransaction(db -> db.execute()
                                                         .sql(INSERT_QUERY)
                                                         .bind("$1", model.getUsername())
                                                         .bind("$2", model.getPassword())
                                                         .fetch()
                                                         .rowsUpdated());
    }

    @Override
    public Flux<Integer> update(User model) {
        return this.databaseClient.inTransaction(db -> db.execute()
                                                         .sql(UPDATE_QUERY)
                                                         .bind("$1", model.getId())
                                                         .bind("$2", model.getUsername())
                                                         .bind("$3", model.getPassword())
                                                         .fetch()
                                                         .rowsUpdated());
    }

    @Override
    public Flux<Integer> delete(Long id) {
        return this.databaseClient.inTransaction(db -> db.execute()
                                                         .sql(DELETE_QUERY)
                                                         .bind("$1", id)
                                                         .fetch()
                                                         .rowsUpdated());
    }
}
