package com.romanidze.siteinfo.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * 04.11.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */

public class DateUtil {

    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d.MM.yyyy");

    public LocalDate mapFromString(String dateString){
        return LocalDate.parse(dateString, this.formatter);
    }

    public String mapFromLocalDate(LocalDate localDate){
        return localDate.format(this.formatter);
    }

    public List<String> performPeriodOfDates(String startDate, String endDate){

        LocalDate startLocalDate = mapFromString(startDate);

        List<String> datesList = new ArrayList<>();
        datesList.add(startDate);
        datesList.add(endDate);

        String tempDate = startDate;

        while(!tempDate.equals(endDate)){

            startLocalDate = startLocalDate.plusDays(1);
            datesList.add(mapFromLocalDate(startLocalDate));
            tempDate = mapFromLocalDate(startLocalDate);

        }

        return datesList;

    }

}
