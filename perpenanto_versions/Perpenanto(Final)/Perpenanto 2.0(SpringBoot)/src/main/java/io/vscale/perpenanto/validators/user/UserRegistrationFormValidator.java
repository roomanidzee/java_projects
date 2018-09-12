package io.vscale.perpenanto.validators.user;

import io.vscale.perpenanto.utils.userutils.FieldValidatorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import io.vscale.perpenanto.forms.user.UserRegistrationForm;
import io.vscale.perpenanto.models.usermodels.User;
import io.vscale.perpenanto.repositories.interfaces.UserRepository;

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

    private final UserRepository userRepository;
    private final FieldValidatorUtils fieldValidatorUtils;

    private final String EMAIL_PATTERN =
            "^[_A-Za-z0-9-+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    @Autowired
    public UserRegistrationFormValidator(UserRepository userRepository, FieldValidatorUtils fieldValidatorUtils) {
        this.userRepository = userRepository;
        this.fieldValidatorUtils = fieldValidatorUtils;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.getName().equals(UserRegistrationForm.class.getName());
    }

    @Override
    public void validate(Object target, Errors errors) {

        UserRegistrationForm form = (UserRegistrationForm)target;

        Optional<User> existedUser = this.userRepository.findByLogin(form.getLogin());

        if(existedUser.isPresent()){
            errors.reject("bad.login", "Данный логин занят");
        }

        Field[] fields = UserRegistrationForm.class.getDeclaredFields();

        List<String> fieldsNames = Arrays.stream(fields)
                                         .map(Field::getName)
                                         .collect(Collectors.toList());

        List<String> errorTypes = new ArrayList<>();
        List<String> errorDescriptions = new ArrayList<>();

        this.fieldValidatorUtils.validateEmptyFiels(fieldsNames, errorTypes, errorDescriptions);

        IntStream.range(0, fieldsNames.size()).forEach(i ->
                ValidationUtils.rejectIfEmptyOrWhitespace(
                        errors, fieldsNames.get(i), errorTypes.get(i), errorDescriptions.get(i)
                )
        );

        Pattern pattern = Pattern.compile(this.EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(form.getEmail());

        if(!matcher.matches()){
            errors.reject("bad.email", "Введите правильно адрес электронной почты");
        }

    }
}
