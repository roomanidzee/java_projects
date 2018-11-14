package com.romanidze.reactivecontractsmongo.utils;

import com.romanidze.reactivecontractsmongo.domain.Contract;
import com.romanidze.reactivecontractsmongo.dto.ContractDTO;

/**
 * 28.10.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
public class EntityMapper {

    public static ContractDTO mapContract(Contract contract){

        return ContractDTO.builder()
                          .title(contract.getTitle())
                          .priceOfContract(contract.getPriceOfContract())
                          .contractDuration(contract.getContractDuration())
                          .activeContractStatus(contract.getActiveContractStatus())
                          .build();

    }

}
