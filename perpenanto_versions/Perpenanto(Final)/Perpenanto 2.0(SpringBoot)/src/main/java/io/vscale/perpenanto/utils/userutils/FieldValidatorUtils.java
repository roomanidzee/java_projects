package io.vscale.perpenanto.utils.userutils;

import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 24.03.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Component
public class FieldValidatorUtils {

    public void validateEmptyFiels(List<String> fieldsNames, List<String> errorTypes, List<String> errorDescriptions){

        StringBuilder sb1 = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();

        fieldsNames.forEach(fieldName -> {

            sb1.append("empty.").append(fieldName);
            sb2.append("Empty ").append(fieldName);
            errorTypes.add(sb1.toString());
            errorDescriptions.add(sb2.toString());
            sb1.setLength(0);
            sb2.setLength(0);

        });

    }

}
