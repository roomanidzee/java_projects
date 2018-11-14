package com.romanidze.reactivecontractsrabbit.services.interfaces;

import com.romanidze.reactivecontractsrabbit.dto.ContractDTO;

/**
 * 05.11.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */

public interface ContractService {

    void sendContract(ContractDTO contractDTO);

}
