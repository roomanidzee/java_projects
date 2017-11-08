package com.romanidze.perpenanto.utils.comparators;

import java.util.List;
import java.util.Map;
import java.util.function.Function;

public interface CompareAttributes<M> {
    List<M> sortList(List<M> oldList, Map<String, Function<M, String>> functionMap, String sortType);
}
