package com.romanidze.reactivecontractsrabbit.receivers;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.romanidze.reactivecontractsrabbit.dto.ContractDTO;
import com.romanidze.reactivecontractsrabbit.services.interfaces.ContractService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 05.11.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Component
public class ContractReceiver {

    private static final Logger logger = LogManager.getLogger(ContractReceiver.class);

    private final ObjectMapper objectMapper;
    private final ContractService contractService;

    @Autowired
    public ContractReceiver(ObjectMapper objectMapper, ContractService contractService) {
        this.objectMapper = objectMapper;
        this.contractService = contractService;
    }

    @RabbitListener(queues = {"${rabbit-service.queue-name}"})
    public void sendContractToOtherServices(byte[] message){

        String jsonMessage = new String(message);
        logger.info("Получен JSON - объект: {}", jsonMessage);

        try{

            ContractDTO contractDTO = this.objectMapper.readValue(jsonMessage, ContractDTO.class);
            this.contractService.sendContract(contractDTO);

        }catch(IOException e){
            logger.error(new IllegalArgumentException(e));
        }

    }

}
