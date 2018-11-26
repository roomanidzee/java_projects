package com.romanidze.siteinfo.services.implementations;

import com.romanidze.siteinfo.services.interfaces.MeasurementRedisService;
import com.romanidze.siteinfo.utils.DateUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ReactiveRedisOperations;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.math.MathFlux;

import java.text.MessageFormat;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * 04.11.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Service
public class MeasurementRedisServiceImpl implements MeasurementRedisService {

    private String uniqueUserKey = "unique:{0}:{1}:({2})";
    private String searchKey = "*({0})";

    private final ReactiveRedisOperations<String, String> usersOperations;

    @Autowired
    public MeasurementRedisServiceImpl(ReactiveRedisOperations<String, String> usersOperations) {
        this.usersOperations = usersOperations;
    }

    @Override
    public Mono<Boolean> hasUser(Long userID, Long pageID, String date) {
        return this.usersOperations.hasKey(MessageFormat.format(this.uniqueUserKey, userID, pageID, date));
    }

    @Override
    public Mono<Boolean> saveUniqueUser(Long userID, Long pageID, String date) {
        return this.usersOperations.opsForValue().set(MessageFormat.format(this.uniqueUserKey, userID, pageID, date), date);
    }

    @Override
    public Mono<Long> countUsersByDate(String date) {
        return this.usersOperations.keys(MessageFormat.format(this.searchKey, date)).count();
    }

    @Override
    public Mono<Long> countUsersByPeriod(String startDate, String endDate) {

        DateUtil dateUtil = new DateUtil();
        List<String> periodStrings = dateUtil.performPeriodOfDates(startDate, endDate);

        AtomicReference<Flux<Long>> tempResult = new AtomicReference<>();
        Flux<Long> tempFlux = Flux.just(0L);
        tempResult.set(tempFlux);

        periodStrings.forEach(s -> tempResult.set(Flux.concat(tempResult.get(), countUsersByDate(s))));

        return MathFlux.sumLong(tempResult.get());

    }
}
