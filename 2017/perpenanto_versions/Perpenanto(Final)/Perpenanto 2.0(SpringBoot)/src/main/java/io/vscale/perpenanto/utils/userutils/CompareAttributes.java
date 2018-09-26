package io.vscale.perpenanto.utils.userutils;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * 29.01.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
public interface CompareAttributes<M> {
    List<M> sortList(List<M> oldList, Map<String, Function<M, String>> functionMap, String sortType);
}
