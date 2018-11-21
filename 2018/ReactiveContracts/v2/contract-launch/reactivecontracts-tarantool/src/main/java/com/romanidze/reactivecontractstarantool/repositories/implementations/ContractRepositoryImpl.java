package com.romanidze.reactivecontractstarantool.repositories.implementations;

import com.romanidze.reactivecontractstarantool.config.tarantool.TarantoolProperties;
import com.romanidze.reactivecontractstarantool.domain.Contract;
import com.romanidze.reactivecontractstarantool.repositories.interfaces.ContractRepository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import org.tarantool.TarantoolClient;
import org.tarantool.TarantoolClientOps;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.Future;

/**
 * 17.11.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Repository
public class ContractRepositoryImpl implements ContractRepository {

    private static final Logger logger = LogManager.getLogger(ContractRepositoryImpl.class);

    private final TarantoolClient tarantoolClient;
    private final TarantoolProperties tarantoolProperties;

    @Autowired
    public ContractRepositoryImpl(TarantoolClient tarantoolClient, TarantoolProperties tarantoolProperties) {
        this.tarantoolClient = tarantoolClient;
        this.tarantoolProperties = tarantoolProperties;
    }

    @Override
    public void save(Contract model) {

        TarantoolClientOps<Integer, List<?>, Object, Future<List<?>>> tarantoolClientOps = this.tarantoolClient.asyncOps();
        logger.info("Сохраняю контракт: {}", model);

        tarantoolClientOps.insert(this.tarantoolProperties.getSpaceID(),  Collections.singletonList(model));

    }
}
