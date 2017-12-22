package ru.itis.romanov_andrey.perpenanto.utils;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 17.11.2017
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Component
public class PasswordGenerator {

    private List<Integer> validCharacters = new ArrayList<>();
    private int passwordLength = 10;

    public String generate(){

        IntStream.rangeClosed('0', '9').forEachOrdered(this.validCharacters::add);
        IntStream.rangeClosed('A', 'Z').forEachOrdered(this.validCharacters::add);
        IntStream.rangeClosed('a', 'z').forEachOrdered(this.validCharacters::add);
        IntStream.rangeClosed('!', '*').forEachOrdered(this.validCharacters::add);

        int[] intArray = new SecureRandom().ints(this.passwordLength, 0, this.validCharacters.size())
                                           .map(this.validCharacters::get)
                                           .toArray();

        return Arrays.stream(intArray)
                     .mapToObj(c -> Character.toString(Character.forDigit(c, 10)))
                     .collect(Collectors.joining());
    }

}
