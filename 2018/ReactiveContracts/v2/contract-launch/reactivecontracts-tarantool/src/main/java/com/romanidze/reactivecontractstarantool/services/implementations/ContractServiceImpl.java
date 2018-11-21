package com.romanidze.reactivecontractstarantool.services.implementations;

import com.romanidze.reactivecontractstarantool.domain.Contract;
import com.romanidze.reactivecontractstarantool.repositories.interfaces.ContractRepository;
import com.romanidze.reactivecontractstarantool.services.interfaces.ContractService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Mono;

/**
 * 17.11.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Service
public class ContractServiceImpl implements ContractService {

    private final ContractRepository contractRepository;

    @Autowired
    public ContractServiceImpl(ContractRepository contractRepository) {
        this.contractRepository = contractRepository;
    }

    @Override
    public Mono<String> saveContract(Contract contract) {

        this.contractRepository.save(contract);

        return Mono.just("{\"message\": \"Контракт сохранён\"}");

    }
}
