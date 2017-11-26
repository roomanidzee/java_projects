package ru.itis.romanov_andrey.perpenanto.utils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 04.11.2017
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
public class StreamCompareAttributes<M> implements CompareAttributes<M> {

    @Override
    public List<M> sortList(List<M> oldList, Map<String, Function<M, String>> functionMap, String sortType) {

        List<M> resultList = new ArrayList<>();

        switch(sortType){

            case "-1":

                resultList.addAll(oldList);
                break;

            case "0":

                resultList = oldList.stream()
                                    .sorted(Comparator.comparing(functionMap.get("0")).reversed())
                                    .collect(Collectors.toList());
                break;

            case "1":

                resultList = oldList.stream()
                                    .sorted(Comparator.comparing(functionMap.get("1")))
                                    .collect(Collectors.toList());
                break;

            case "2":

                resultList = oldList.stream()
                                    .sorted(Comparator.comparing(functionMap.get("2")))
                                    .collect(Collectors.toList());
                break;

        }

        return resultList;
    }
}
