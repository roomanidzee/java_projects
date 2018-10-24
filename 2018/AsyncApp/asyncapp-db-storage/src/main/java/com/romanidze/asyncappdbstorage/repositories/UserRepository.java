package com.romanidze.asyncappdbstorage.repositories;

import com.romanidze.asyncappdbstorage.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.scheduling.annotation.Async;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * 04.10.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */

public interface UserRepository extends JpaRepository<User, Long> {

    @Async
    CompletableFuture<List<User>> readAllBy();

    Optional<User> findByUsername(String username);

}
