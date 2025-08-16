package com.va2es.backend.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PhoneValidator implements ConstraintValidator<Phone, String> {

    @Override
    public boolean isValid(String telefone, ConstraintValidatorContext context) {
        if (telefone == null) {
            return true;
        }
        String telefoneRegex = "^\\d{10,11}$";
        return telefone.matches(telefoneRegex);
    }
}
