package com.romanidze.siteinfo.services.interfaces;

import reactor.core.publisher.Mono;

/**
 * 04.11.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */

public interface MeasurementRedisService {

    Mono<Boolean> hasUser(Long userID, Long pageID, String date);
    Mono<Boolean> saveUniqueUser(Long userID, Long pageID, String date);
    Mono<Long> countUsersByDate(String date);
    Mono<Long> countUsersByPeriod(String startDate, String endDate);

}
