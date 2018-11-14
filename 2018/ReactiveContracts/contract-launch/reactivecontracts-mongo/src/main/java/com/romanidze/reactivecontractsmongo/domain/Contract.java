package com.romanidze.reactivecontractsmongo.domain;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.ToString;

import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.annotation.Id;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * 28.10.2018
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
@Document(collection = "contracts")
@TypeAlias("contract")
public class Contract {

    @Id
    private String id;

    @Field("title")
    private String title;

    @Field("price_of_contract")
    private Long priceOfContract;

    @Field("contract_duration")
    private Long contractDuration;

    @Field("active_contract")
    private Boolean activeContractStatus;

}
