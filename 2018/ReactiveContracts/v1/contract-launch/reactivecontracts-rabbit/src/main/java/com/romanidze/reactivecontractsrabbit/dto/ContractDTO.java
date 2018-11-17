package com.romanidze.reactivecontractsrabbit.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.ToString;

/**
 * 05.11.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@EqualsAndHashCode
@ToString
public class ContractDTO {

    private String title;

    @JsonProperty("price_of_contract")
    private Long priceOfContract;

    @JsonProperty("contract_duration")
    private Long contractDuration;

    @JsonProperty("active_contract")
    private Boolean activeContractStatus;

}
