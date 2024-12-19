package com.frieghtfox.validation.validators;
import com.frieghtfox.validation.annotation.ValidEquation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
public class EquationSyntaxValidator implements ConstraintValidator<ValidEquation, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            return false; 
        }


        String regex = "^[a-zA-Z0-9+\\-*/^().\\s]+$";


        if (!value.matches(regex)) {
            return false;
        }


        return hasBalancedParentheses(value) && isValidOperatorPlacement(value);
    }

    private boolean hasBalancedParentheses(String equation) {
        int count = 0;
        for (char c : equation.toCharArray()) {
            if (c == '(') count++;
            if (c == ')') count--;
            if (count < 0) return false; 
        }
        return count == 0;
    }

    private boolean isValidOperatorPlacement(String equation) {
        String invalidPatterns = ".*[+\\-*/^]{2,}.*|^[+\\-*/^]|[+\\-*/^]$";
        return !equation.matches(invalidPatterns);
    }
}
