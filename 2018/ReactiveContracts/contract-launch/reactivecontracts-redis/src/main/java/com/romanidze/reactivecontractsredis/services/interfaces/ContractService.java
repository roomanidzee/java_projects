package com.romanidze.reactivecontractsredis.services.interfaces;

import com.romanidze.reactivecontractsredis.domain.Contract;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 28.10.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */

public interface ContractService {

    Flux<Contract> getAllContracts();
    Mono<Contract> getContractByID(String id);
    Mono<String> sendToRedis(Contract contract);

}
