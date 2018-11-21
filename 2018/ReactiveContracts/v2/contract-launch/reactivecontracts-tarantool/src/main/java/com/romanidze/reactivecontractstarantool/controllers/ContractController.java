package com.romanidze.reactivecontractstarantool.controllers;

import com.romanidze.reactivecontractstarantool.domain.Contract;
import com.romanidze.reactivecontractstarantool.services.interfaces.ContractService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

/**
 * 17.11.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */

@RestController
public class ContractController {

    private final ContractService contractService;

    @Autowired
    public ContractController(ContractService contractService) {
        this.contractService = contractService;
    }

    @PostMapping("/save")
    public Mono<ResponseEntity<String>> saveContract(@RequestBody Contract contract){

        return this.contractService.saveContract(contract)
                                   .map(ResponseEntity::ok);

    }
}
