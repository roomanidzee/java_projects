package com.romanidze.siteinfo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.ToString;

import javax.validation.constraints.Pattern;

/**
 * 03.11.2018
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
public class PeriodInputMeasurementDTO {

    @JsonProperty("start_date")
    @Pattern(regexp = "^\\s*(3[01]|[12][0-9]|0?[1-9])\\.(1[012]|0?[1-9])\\.((?:19|20)\\d{2})\\s*$",
             message = "Дата начала периода учёта указана в неверном формате")
    private String startDate;

    @JsonProperty("end_date")
    @Pattern(regexp = "^\\s*(3[01]|[12][0-9]|0?[1-9])\\.(1[012]|0?[1-9])\\.((?:19|20)\\d{2})\\s*$",
             message = "Дата окончания периода учёта указана в неверном формате")
    private String endDate;

}
