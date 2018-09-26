package ru.itis.romanov_andrey.perpenanto.utils;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * 04.11.2017
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
public interface CompareAttributes<M> {
    List<M> sortList(List<M> oldList, Map<String, Function<M, String>> functionMap, String sortType);
}
