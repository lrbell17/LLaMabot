package com.lrbell.llamabot.dto.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validator to make sure password is in the correct format.
 */
public class StrongPasswordValidator implements ConstraintValidator<StrongPassword, String> {


    /**
     * Regex pattern checking if length is between 8-32 chars, and for a upper, lower, numeric and special char
     */
    private static final String PATTERN =
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$!%*?&])[A-Za-z\\d@#$!%*?&]{8,32}$";

    @Override
    public void initialize(final StrongPassword constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(final String raw, final ConstraintValidatorContext context) {
        return raw != null && raw.matches(PATTERN);
    }
}
