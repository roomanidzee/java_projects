package com.romanidze.siteinfo.services.implementations;

import com.romanidze.siteinfo.domain.Measurement;
import com.romanidze.siteinfo.dto.MeasurementDTO;
import com.romanidze.siteinfo.dto.PeriodInputMeasurementDTO;
import com.romanidze.siteinfo.dto.PeriodOutputMeasurementDTO;
import com.romanidze.siteinfo.dto.SingleMeasurementDTO;
import com.romanidze.siteinfo.repositories.MeasurementPostgreSQLRepository;
import com.romanidze.siteinfo.services.interfaces.MeasurementRedisService;
import com.romanidze.siteinfo.services.interfaces.MeasurementService;
import com.romanidze.siteinfo.utils.DateUtil;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.ZoneId;

/**
 * 03.11.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Service
public class MeasurementServiceImpl implements MeasurementService{

    public static final Logger logger = LogManager.getLogger(MeasurementServiceImpl.class);

    private final MeasurementPostgreSQLRepository measurementPostgreSQLRepository;
    private final MeasurementRedisService measurementRedisService;

    @Autowired
    public MeasurementServiceImpl(MeasurementPostgreSQLRepository measurementPostgreSQLRepository,
                                  MeasurementRedisService measurementRedisService) {
        this.measurementPostgreSQLRepository = measurementPostgreSQLRepository;
        this.measurementRedisService = measurementRedisService;
    }

    @Override
    public Mono<SingleMeasurementDTO> getSingleMeasurement(MeasurementDTO measurementDTO) {

        DateUtil dateUtil = new DateUtil();
        String currentDate = dateUtil.mapFromLocalDate(LocalDate.now(ZoneId.of("Europe/Moscow")));

        Measurement measurement = Measurement.builder()
                                             .userID(measurementDTO.getUserID())
                                             .pageID(measurementDTO.getPageID())
                                             .attendanceDate(currentDate)
                                             .build();

        this.measurementPostgreSQLRepository.save(measurement)
                                            .subscribe(null,
                                                       logger::error,
                                                       () -> logger.info("Информация сохранена"));

        Flux<Measurement> existedMeasurements =
                this.measurementPostgreSQLRepository.findAllByAttendanceDateAndPageID(currentDate, measurementDTO.getPageID());

        SingleMeasurementDTO singleMeasurementDTO = new SingleMeasurementDTO();

        existedMeasurements.count()
                           .subscribe(singleMeasurementDTO::setAttendanceCount,
                                      logger::error,
                                      () -> logger.info("Количество посещений за текущие сутки получено"));



        Mono<Boolean> existedUniqueUser = this.measurementRedisService.hasUser(measurementDTO.getUserID(),
                                                                                  measurementDTO.getPageID(),
                                                                                  currentDate);

        existedUniqueUser.subscribe(condition -> {

            if(!condition){

                Mono<Boolean> uniqueUserOperation =
                        this.measurementRedisService.saveUniqueUser(measurementDTO.getUserID(),
                                                                     measurementDTO.getPageID(),
                                                                    currentDate);

                uniqueUserOperation.subscribe(null,
                                              logger::error,
                                              () -> logger.info("Уникальный пользователь был сохранён"));

            }

        },
        logger::error,
        () -> logger.info("Проверка уникальности пользователя прошла"));

        Mono<Long> uniqueUserCount =
                this.measurementRedisService.countUsersByDate(currentDate);

        uniqueUserCount.subscribe(item -> {

            if(item == null){
               singleMeasurementDTO.setUniqueUserCount(0L);
            }else{
                singleMeasurementDTO.setUniqueUserCount(item);
            }

        },
        logger::error,
        () -> logger.info("Количество уникальных пользователей за сутки получено"));

        return Mono.just(singleMeasurementDTO);

    }

    @Override
    public Mono<PeriodOutputMeasurementDTO> getPeriodMeasurement(PeriodInputMeasurementDTO periodDTO) {

        String startDate = periodDTO.getStartDate();
        String endDate = periodDTO.getEndDate();

        PeriodOutputMeasurementDTO periodOutputDTO = new PeriodOutputMeasurementDTO();

        Flux<Measurement> usersByPeriod = this.measurementPostgreSQLRepository.findByAttendanceDateBetween(startDate, endDate);

        usersByPeriod.count()
                     .subscribe(periodOutputDTO::setAttendanceCount,
                                logger::error,
                                () -> logger.info("Количество посещений за указанный период получено"));

        Mono<Long> uniqueUserCount =
                this.measurementRedisService.countUsersByPeriod(periodDTO.getStartDate(), periodDTO.getEndDate());

        uniqueUserCount.subscribe(periodOutputDTO::setUniqueUserCount,
                                  logger::error,
                                  () -> logger.info("Количество уникальных пользователей за период получено"));

        Flux<Measurement> regularUsers = this.measurementPostgreSQLRepository.findRegularUsers(startDate, endDate);

        regularUsers.count()
                    .subscribe(periodOutputDTO::setStableUserCount,
                               logger::error,
                               () -> logger.info("Количество стабильных пользователей за указанный период получено"));

        return Mono.just(periodOutputDTO);

    }
}
