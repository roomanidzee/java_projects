package com.romanidze.reactivecontractstarantool.repositories.interfaces;

import com.romanidze.reactivecontractstarantool.domain.Contract;

/**
 * 17.11.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */

public interface ContractRepository extends CRUDRepository<Contract, Long> {

    @Override
    default Contract findOne(Long id) {
        return null;
    }

    @Override
    default void update(Long id, Contract model){}

    @Override
    default void delete(Long id){}
}
