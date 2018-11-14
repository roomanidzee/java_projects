package com.romanidze.reactivecontractsmongo.services.implementations;

import com.romanidze.reactivecontractsmongo.domain.Contract;
import com.romanidze.reactivecontractsmongo.dto.ContractDTO;
import com.romanidze.reactivecontractsmongo.dto.ContractRedisDTO;
import com.romanidze.reactivecontractsmongo.repositories.ContractRepository;
import com.romanidze.reactivecontractsmongo.services.interfaces.ContractService;
import com.romanidze.reactivecontractsmongo.utils.EntityMapper;

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

        ContractRedisDTO contractRedisDTO = new ContractRedisDTO();

        Mono<Contract> savedContract = this.contractRepository.save(contract);
        Mono<ContractDTO> resultDTO = savedContract.map(EntityMapper::mapContract);

        savedContract.map(contract1 -> {
            contractRedisDTO.setId(contract1.getId());
            contractRedisDTO.setContractDuration(contract1.getContractDuration());
            return Mono.just(contractRedisDTO);
        }).subscribe();

        this.webClient.post()
                      .uri("/save")
                      .body(BodyInserters.fromObject(contractRedisDTO))
                      .exchange()
                      .subscribe();

        return resultDTO;
    }

    @Override
    public Flux<ContractDTO> getAllContracts() {

        Flux<Contract> contracts = this.contractRepository.findAll();

        return contracts.map(EntityMapper::mapContract);
    }
}
