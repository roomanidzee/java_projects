package com.romanidze.reactivchina.repositories.interfaces;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 08.12.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */

public interface CRUDRepository<T, ID> {

    Flux<T> findAll();
    Mono<T> find(ID id);
    Flux<Integer> save(T model);
    Flux<Integer> update(T model);
    Flux<Integer> delete(ID id);

}
