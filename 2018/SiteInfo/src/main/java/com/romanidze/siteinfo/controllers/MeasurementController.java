package com.romanidze.siteinfo.controllers;

import com.romanidze.siteinfo.dto.MeasurementDTO;
import com.romanidze.siteinfo.dto.PeriodInputMeasurementDTO;
import com.romanidze.siteinfo.dto.PeriodOutputMeasurementDTO;
import com.romanidze.siteinfo.dto.SingleMeasurementDTO;
import com.romanidze.siteinfo.services.interfaces.MeasurementService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

/**
 * 04.11.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@RestController
@RequestMapping("/measurements")
public class MeasurementController {

    private final MeasurementService measurementService;

    @Autowired
    public MeasurementController(MeasurementService measurementService) {
        this.measurementService = measurementService;
    }

    @PostMapping("/user_request")
    public Mono<ResponseEntity<SingleMeasurementDTO>> getPageInformation(@RequestBody MeasurementDTO measurementDTO){

        return this.measurementService.getSingleMeasurement(measurementDTO)
                                      .map(ResponseEntity::ok);

    }

    @PostMapping("/period_statistics")
    public Mono<ResponseEntity<PeriodOutputMeasurementDTO>> getPeriodInformation(
                                                    @RequestBody PeriodInputMeasurementDTO periodDTO){

        return this.measurementService.getPeriodMeasurement(periodDTO)
                                      .map(ResponseEntity::ok);

    }

}
