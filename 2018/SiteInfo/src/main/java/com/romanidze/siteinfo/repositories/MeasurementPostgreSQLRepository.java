package com.romanidze.siteinfo.repositories;

import com.romanidze.siteinfo.domain.Measurement;

import org.springframework.data.r2dbc.repository.query.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import reactor.core.publisher.Flux;

/**
 * 03.11.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */

public interface MeasurementPostgreSQLRepository extends ReactiveCrudRepository<Measurement, Long> {

    @Query("SELECT * FROM measurement WHERE attendance_date = $1 AND page_id = $2")
    Flux<Measurement> findAllByAttendanceDateAndPageID(String attendanceDate, Long pageID);

    @Query("SELECT * FROM measurement WHERE attendance_date > $1 AND attendance_date < $2")
    Flux<Measurement> findByAttendanceDateBetween(String startDate, String endDate);

    @Query("SELECT * FROM measurement WHERE attendance_date >= $1 AND attendance_date <= $2" +
            " GROUP BY id, user_id HAVING COUNT(page_id) > 10")
    Flux<Measurement> findRegularUsers(String start, String end);

}
