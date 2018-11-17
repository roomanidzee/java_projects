package com.romanidze.reactivecontractsredis.services.implementations;

import com.romanidze.reactivecontractsredis.domain.Contract;
import com.romanidze.reactivecontractsredis.messaging.publisher.ContractInfoPublisher;
import com.romanidze.reactivecontractsredis.services.interfaces.ContractService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

/**
 * 28.10.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Service
public class ContractServiceImpl implements ContractService {

    private final ReactiveRedisOperations<String, Contract> contractOperations;
    private final ContractInfoPublisher contractInfoPublisher;

    @Autowired
    public ContractServiceImpl(ReactiveRedisOperations<String, Contract> contractOperations,
                               ContractInfoPublisher contractInfoPublisher) {
        this.contractOperations = contractOperations;
        this.contractInfoPublisher = contractInfoPublisher;
    }

    @Override
    public Flux<Contract> getAllContracts() {
        return this.contractOperations.keys("*")
                                      .flatMap(this.contractOperations.opsForValue()::get);
    }

    @Override
    public Mono<Contract> getContractByID(String id) {
        return this.contractOperations.opsForValue().get(id);
    }

    @Override
    public Mono<String> sendToRedis(Contract contract) {

        this.contractInfoPublisher.setContract(contract);
        this.contractInfoPublisher.publish();

        return Mono.just("{\"message\": \"Контракт отправлен в Redis\"}");

    }
}
