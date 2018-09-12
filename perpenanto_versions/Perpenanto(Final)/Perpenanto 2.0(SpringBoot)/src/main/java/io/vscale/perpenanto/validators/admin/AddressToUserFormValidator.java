package io.vscale.perpenanto.validators.admin;

import io.vscale.perpenanto.forms.admin.AddressToUserForm;
import io.vscale.perpenanto.models.usermodels.User;
import io.vscale.perpenanto.repositories.interfaces.UserRepository;
import io.vscale.perpenanto.utils.userutils.FieldValidatorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 24.03.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Component
public class AddressToUserFormValidator implements Validator{

    private final UserRepository userRepository;
    private final FieldValidatorUtils fieldValidatorUtils;

    @Autowired
    public AddressToUserFormValidator(UserRepository userRepository, FieldValidatorUtils fieldValidatorUtils) {
        this.userRepository = userRepository;
        this.fieldValidatorUtils = fieldValidatorUtils;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.getName().equals(AddressToUserForm.class.getName());
    }

    @Override
    public void validate(Object target, Errors errors) {

        AddressToUserForm form = (AddressToUserForm) target;
        Optional<User> existedUser = this.userRepository.findById(form.getUserId());

        if(!existedUser.isPresent()){
            errors.reject("bad.input", "Такого пользователя не существует");
        }

        Field[] fields = AddressToUserForm.class.getDeclaredFields();

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

    }
}
