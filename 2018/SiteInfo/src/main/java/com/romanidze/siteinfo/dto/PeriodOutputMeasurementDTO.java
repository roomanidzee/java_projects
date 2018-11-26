package com.romanidze.siteinfo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.Builder;
import lombok.ToString;

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
public class PeriodOutputMeasurementDTO {

    @JsonProperty("attendance_count")
    private Long attendanceCount;

    @JsonProperty("unique_user_count")
    private Long uniqueUserCount;

    @JsonProperty("stable_user_count")
    private Long stableUserCount;

}
