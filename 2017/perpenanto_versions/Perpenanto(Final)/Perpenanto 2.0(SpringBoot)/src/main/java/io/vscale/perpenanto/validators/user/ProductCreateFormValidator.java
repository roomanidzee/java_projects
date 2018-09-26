package io.vscale.perpenanto.validators.user;

import io.vscale.perpenanto.forms.user.ProductCreateForm;
import io.vscale.perpenanto.models.usermodels.Product;
import io.vscale.perpenanto.repositories.interfaces.ProductRepository;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * 24.03.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Component
public class ProductCreateFormValidator implements Validator{

    private final ProductRepository productRepository;
    private final FieldValidatorUtils fieldValidatorUtils;

    private final String URL_PATTERN = "(https?:\\/\\/.*\\.(?:png|jpg))";

    @Autowired
    public ProductCreateFormValidator(ProductRepository productRepository, FieldValidatorUtils fieldValidatorUtils) {
        this.productRepository = productRepository;
        this.fieldValidatorUtils = fieldValidatorUtils;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.getName().equals(ProductCreateForm.class.getName());
    }

    @Override
    public void validate(Object target, Errors errors) {

        ProductCreateForm form = (ProductCreateForm) target;
        Product product = this.productRepository.findByTitle(form.getTitle());

        if(product == null){
            errors.reject("bad.title", "Такой товар уже существует");
        }

        if(form.getPrice() < 0){
            errors.reject("bad.price", "Цена должна быть больше нуля!");
        }

        Pattern pattern = Pattern.compile(this.URL_PATTERN);
        Matcher matcher = pattern.matcher(form.getPhotolink());

        if(!matcher.matches()){
            errors.reject("bad.link", "Неверно введена ссылка на фото");
        }

        Field[] fields = ProductCreateForm.class.getDeclaredFields();

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
