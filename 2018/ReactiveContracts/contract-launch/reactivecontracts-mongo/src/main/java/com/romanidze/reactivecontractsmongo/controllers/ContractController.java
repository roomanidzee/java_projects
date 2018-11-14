package com.romanidze.reactivecontractsmongo.controllers;

import com.romanidze.reactivecontractsmongo.dto.ContractDTO;
import com.romanidze.reactivecontractsmongo.services.interfaces.ContractService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 28.10.2018
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
    public Flux<ResponseEntity<ContractDTO>> getAllContracts(){

        return this.contractService.getAllContracts()
                                   .map(ResponseEntity::ok);

    }

    @PostMapping("/save")
    public Mono<ResponseEntity<ContractDTO>> saveContract(@RequestBody ContractDTO contractDTO){

        return this.contractService.saveContract(contractDTO)
                                   .map(ResponseEntity::ok);

    }

    @GetMapping("/{title}")
    public Mono<ResponseEntity<ContractDTO>> getContractByTitle(@PathVariable("title") String title){

        return this.contractService.getContractByTitle(title)
                                   .map(ResponseEntity::ok);

    }
}
