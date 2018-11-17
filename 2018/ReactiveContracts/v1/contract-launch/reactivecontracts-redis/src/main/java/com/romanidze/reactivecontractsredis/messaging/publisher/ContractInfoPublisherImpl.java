package com.romanidze.reactivecontractsredis.messaging.publisher;

import com.romanidze.reactivecontractsredis.domain.Contract;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Component;

/**
 * 05.11.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Component
public class ContractInfoPublisherImpl implements ContractInfoPublisher{

    private Contract contract;

    private final ReactiveRedisOperations<String, Contract> contractOperations;
    private final ChannelTopic channelTopic;

    @Autowired
    public ContractInfoPublisherImpl(ReactiveRedisOperations<String, Contract> contractOperations, ChannelTopic channelTopic) {
        this.contractOperations = contractOperations;
        this.channelTopic = channelTopic;
    }

    @Override
    public void publish() {
        this.contractOperations.convertAndSend(this.channelTopic.getTopic(), this.contract)
                               .subscribe();
    }

    @Override
    public void setContract(Contract contract) {
        this.contract = contract;
    }
}
