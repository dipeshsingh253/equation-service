package com.frieghtfox.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

import com.frieghtfox.validation.validators.EquationSyntaxValidator;

@Documented
@Constraint(validatedBy = EquationSyntaxValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidEquation {
    String message() default "Equation contains invalid syntax";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}