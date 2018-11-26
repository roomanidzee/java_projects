package com.romanidze.siteinfo.services.interfaces;

import com.romanidze.siteinfo.dto.MeasurementDTO;
import com.romanidze.siteinfo.dto.PeriodInputMeasurementDTO;
import com.romanidze.siteinfo.dto.PeriodOutputMeasurementDTO;
import com.romanidze.siteinfo.dto.SingleMeasurementDTO;

import reactor.core.publisher.Mono;

/**
 * 03.11.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */

public interface MeasurementService {

    Mono<SingleMeasurementDTO> getSingleMeasurement(MeasurementDTO measurementDTO);
    Mono<PeriodOutputMeasurementDTO> getPeriodMeasurement(PeriodInputMeasurementDTO periodDTO);

}
