package com.va2es.backend.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CNPJValidator implements ConstraintValidator<CNPJ, String> {

    @Override
    public boolean isValid(String cnpj, ConstraintValidatorContext context) {
        if (cnpj == null) {
            return true;
        }
        String cnpjRegex = "(^\\d{2}\\.\\d{3}\\.\\d{3}/\\d{4}-\\d{2}$)|(^\\d{14}$)";
        return cnpj.matches(cnpjRegex);
    }
}
