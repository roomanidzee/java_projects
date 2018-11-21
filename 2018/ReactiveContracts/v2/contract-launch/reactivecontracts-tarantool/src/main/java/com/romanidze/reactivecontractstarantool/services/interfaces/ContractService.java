package com.romanidze.reactivecontractstarantool.services.interfaces;

import com.romanidze.reactivecontractstarantool.domain.Contract;

import reactor.core.publisher.Mono;

/**
 * 17.11.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */

public interface ContractService {

    Mono<String> saveContract(Contract contract);

}
