package ru.itis.romanov_andrey.perpenanto.dto.interfaces;

import java.util.List;

/**
 * 01.01.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
public interface EntityDTOInterface<T, F> {

    public List<T> convert(List<F> originalList);

}
