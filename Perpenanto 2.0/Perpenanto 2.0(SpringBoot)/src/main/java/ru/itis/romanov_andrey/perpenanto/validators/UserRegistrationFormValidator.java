package ru.itis.romanov_andrey.perpenanto.validators;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import ru.itis.romanov_andrey.perpenanto.forms.user.UserRegistrationForm;
import ru.itis.romanov_andrey.perpenanto.models.usermodels.User;
import ru.itis.romanov_andrey.perpenanto.repositories.interfaces.UserRepository;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 29.01.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Component
public class UserRegistrationFormValidator implements Validator{

    @Autowired
    private UserRepository userRepository;

    private String EMAIL_PATTERN =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.getName().equals(UserRegistrationForm.class.getName());
    }

    @Override
    public void validate(Object target, Errors errors) {

        UserRegistrationForm form = (UserRegistrationForm)target;

        Field[] fields = UserRegistrationForm.class.getDeclaredFields();

        List<String> fieldsNames = Arrays.stream(fields)
                                         .map(Field::getName)
                                         .collect(Collectors.toList());

        List<String> errorTypes = new ArrayList<>();
        List<String> errorDescription = new ArrayList<>();

        StringBuilder sb1 = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();

        fieldsNames.forEach(fieldName -> {

            sb1.append("empty.").append(fieldName);
            sb2.append("Empty ").append(fieldName);
            errorTypes.add(sb1.toString());
            errorDescription.add(sb2.toString());
            sb1.setLength(0);
            sb2.setLength(0);

        });

        IntStream.range(0, fieldsNames.size()).forEach(i ->
                ValidationUtils.rejectIfEmptyOrWhitespace(
                        errors, fieldsNames.get(i), errorTypes.get(i), errorDescription.get(i)
                )
        );

        Pattern pattern = Pattern.compile(this.EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(form.getEmail());

        if(!matcher.matches()){
            errors.reject("bad.email", "Введите правильно адрес электронной почты");
        }

    }
}
