package com.romanidze.reactivecontractsredis.controllers;

import com.romanidze.reactivecontractsredis.domain.Contract;
import com.romanidze.reactivecontractsredis.services.interfaces.ContractService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 05.11.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@RestController
@RequestMapping("/contracts")
public class ContractController {

    private final ContractService contractService;

    @Autowired
    public ContractController(ContractService contractService) {
        this.contractService = contractService;
    }

    @GetMapping("/all")
    public Flux<ResponseEntity<Contract>> getAllContracts(){

        return this.contractService.getAllContracts()
                                   .map(ResponseEntity::ok);

    }

    @PostMapping("/save")
    public Mono<ResponseEntity<String>> saveContract(@RequestBody Contract contract){

        return this.contractService.sendToRedis(contract)
                                   .map(ResponseEntity::ok);

    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Contract>> getContractByID(@PathVariable("id") String id){

        return this.contractService.getContractByID(id)
                                   .map(ResponseEntity::ok);

    }

}
