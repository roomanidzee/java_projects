package ru.itis.romanov_andrey.perpenanto.utils;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * 02.12.2017
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Component
public class AdminRequestCheck {

    private List<String> statuses = Arrays.asList("add","update", "delete");

    public boolean checkRequest(String request){

        return this.statuses.stream()
                            .anyMatch(request::equals);

    }

}
