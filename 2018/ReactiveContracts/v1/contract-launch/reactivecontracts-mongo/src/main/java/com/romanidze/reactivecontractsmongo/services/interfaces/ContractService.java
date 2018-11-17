package com.romanidze.reactivecontractsmongo.services.interfaces;

import com.romanidze.reactivecontractsmongo.dto.ContractDTO;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 28.10.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */

public interface ContractService {

    Mono<ContractDTO> getContractByTitle(String title);

    Mono<ContractDTO> saveContract(ContractDTO contractDTO);

    Flux<ContractDTO> getAllContracts();

}
