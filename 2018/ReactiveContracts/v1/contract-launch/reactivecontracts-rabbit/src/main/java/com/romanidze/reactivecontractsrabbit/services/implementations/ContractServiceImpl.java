package com.romanidze.reactivecontractsrabbit.services.implementations;

import com.romanidze.reactivecontractsrabbit.dto.ContractDTO;
import com.romanidze.reactivecontractsrabbit.services.interfaces.ContractService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * 05.11.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Service
public class ContractServiceImpl implements ContractService {

    private static final Logger logger = LogManager.getLogger(ContractServiceImpl.class);

    private final WebClient webClient;

    @Autowired
    public ContractServiceImpl(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public void sendContract(ContractDTO contractDTO) {

          this.webClient.post()
                        .uri("/save")
                        .body(BodyInserters.fromObject(contractDTO))
                        .exchange()
                        .subscribe(clientResponse -> logger.info("Получен ответ: {}", clientResponse),
                                   logger::error,
                                   () -> logger.info("Отправка клиента завершена"));

    }
}
