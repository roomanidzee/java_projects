package com.romanidze.asyncappdbstorage.security.exceptions;

import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 21.10.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */

public class EntityNotFoundException extends Exception{

    public EntityNotFoundException(Class clazz, String... searchParamsMap){
        super(EntityNotFoundException.generateMessage(clazz.getSimpleName(),
                generateMap(String.class, String.class, (Object[]) searchParamsMap)));
    }

    private static String generateMessage(String entity, Map<?, ?> searchParams){
        return "Сущность " + entity + " не была найдена. Атрибуты: " + searchParams.toString();
    }

    private static Map<?, ?> generateMap(Class keyType, Class valueType, Object... entries){

        if(entries.length % 2 == 1){
            throw new IllegalArgumentException("Invalid entries");
        }

        return IntStream.range(0, entries.length / 2)
                        .boxed()
                        .collect(
                              Collectors.toMap(i -> keyType.cast(entries[i]),
                                               i -> valueType.cast(entries[i + 1]),
                                               (a, b) -> b
                                              )
                        );

    }

}
