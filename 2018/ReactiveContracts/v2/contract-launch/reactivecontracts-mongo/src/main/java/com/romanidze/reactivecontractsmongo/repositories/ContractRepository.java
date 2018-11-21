package com.romanidze.reactivecontractsmongo.repositories;

import com.romanidze.reactivecontractsmongo.domain.Contract;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

/**
 * 28.10.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */

public interface ContractRepository extends ReactiveMongoRepository<Contract, String> {

    Mono<Contract> findByTitle(String title);

}
