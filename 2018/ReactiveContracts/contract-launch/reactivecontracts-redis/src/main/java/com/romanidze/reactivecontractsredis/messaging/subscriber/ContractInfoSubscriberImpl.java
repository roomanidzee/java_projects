package com.romanidze.reactivecontractsredis.messaging.subscriber;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.romanidze.reactivecontractsredis.domain.Contract;
import com.romanidze.reactivecontractsredis.services.interfaces.ContractService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.time.Duration;

/**
 * 05.11.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
public class ContractInfoSubscriberImpl implements MessageListener {

    private static final Logger logger = LogManager.getLogger(ContractInfoSubscriberImpl.class);

    private final ReactiveRedisOperations<String, Contract> contractOperations;
    private final ObjectMapper objectMapper;

    public ContractInfoSubscriberImpl(ReactiveRedisOperations<String, Contract> contractOperations,
                                      ObjectMapper objectMapper) {
        this.contractOperations = contractOperations;
        this.objectMapper = objectMapper;
    }

    @Override
    public void onMessage(Message message, byte[] pattern) {

        byte[] messageBody = message.getBody();
        String jsonMessage = new String(messageBody);

        logger.info("Получено сообщение: {}", jsonMessage);

        try{

            Contract contract = this.objectMapper.readValue(jsonMessage, Contract.class);

            Mono<Boolean> saveResult =
                    this.contractOperations.opsForValue().set(contract.getId(), contract,
                                                                    Duration.ofSeconds(contract.getContractDuration()));
            saveResult.subscribe();

            logger.info("Контракт, который сохранён: {}", contract);

        }catch(IOException e){
            logger.error(new IllegalArgumentException(e));
        }

    }
}
