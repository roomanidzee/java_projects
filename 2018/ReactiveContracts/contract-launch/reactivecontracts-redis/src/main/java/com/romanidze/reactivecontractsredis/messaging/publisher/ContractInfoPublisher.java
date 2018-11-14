package com.romanidze.reactivecontractsredis.messaging.publisher;

import com.romanidze.reactivecontractsredis.domain.Contract;

/**
 * 05.11.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */

public interface ContractInfoPublisher {

    void publish();
    void setContract(Contract contract);

}
