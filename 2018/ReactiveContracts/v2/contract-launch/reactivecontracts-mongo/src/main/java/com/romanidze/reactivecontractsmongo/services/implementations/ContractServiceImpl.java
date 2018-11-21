package com.romanidze.reactivecontractsmongo.services.implementations;

import com.romanidze.reactivecontractsmongo.domain.Contract;
import com.romanidze.reactivecontractsmongo.dto.ContractDTO;
import com.romanidze.reactivecontractsmongo.dto.ContractTarantoolDTO;
import com.romanidze.reactivecontractsmongo.repositories.ContractRepository;
import com.romanidze.reactivecontractsmongo.services.interfaces.ContractService;
import com.romanidze.reactivecontractsmongo.utils.EntityMapper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * 28.10.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Service
public class ContractServiceImpl implements ContractService {

    private static final Logger logger = LogManager.getLogger(ContractServiceImpl.class);

    private final ContractRepository contractRepository;
    private final WebClient webClient;

    @Autowired
    public ContractServiceImpl(ContractRepository contractRepository, WebClient webClient) {
        this.contractRepository = contractRepository;
        this.webClient = webClient;
    }

    @Override
    public Mono<ContractDTO> getContractByTitle(String title) {

        Mono<Contract> contract = this.contractRepository.findByTitle(title);

        return contract.map(EntityMapper::mapContract);
    }

    @Override
    public Mono<ContractDTO> saveContract(ContractDTO contractDTO) {

        Contract contract = Contract.builder()
                                    .title(contractDTO.getTitle())
                                    .priceOfContract(contractDTO.getPriceOfContract())
                                    .contractDuration(contractDTO.getContractDuration())
                                    .activeContractStatus(contractDTO.getActiveContractStatus())
                                    .build();

        logger.info("Получен контракт: {}", contractDTO);

        ContractTarantoolDTO contractTarantoolDTO = new ContractTarantoolDTO();

        Mono<Contract> savedContract = this.contractRepository.save(contract);
        Mono<ContractDTO> resultDTO = savedContract.map(EntityMapper::mapContract);

        savedContract.map(contract1 -> {
            contractTarantoolDTO.setId(contract1.getId());
            contractTarantoolDTO.setTitle(contract1.getTitle());
            contractTarantoolDTO.setContractDuration(contract1.getContractDuration());
            return Mono.just(contractTarantoolDTO);
        });

        this.webClient.post()
                      .uri("/save")
                      .body(BodyInserters.fromObject(contractTarantoolDTO))
                      .exchange();

        return resultDTO;
    }

    @Override
    public Flux<ContractDTO> getAllContracts() {

        Flux<Contract> contracts = this.contractRepository.findAll();

        return contracts.map(EntityMapper::mapContract);
    }
}
