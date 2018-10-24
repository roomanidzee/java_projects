package com.romanidze.asyncappdbstorage.validation.constraints;

import com.romanidze.asyncappdbstorage.validation.validators.BannedLoginValidator;

import javax.validation.Constraint;
import javax.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.lang.annotation.RetentionPolicy;

/**
 * 21.10.2018
 *
 * @author Andrey Romanov (steampart@gmail.com)
 * @version 1.0
 */
@Documented
@Constraint(validatedBy = BannedLoginValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface BannedLoginConstraint {
    String message() default "Пользователь с таким логином заблокирован";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
