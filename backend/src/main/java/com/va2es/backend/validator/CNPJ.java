package com.va2es.backend.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CNPJValidator.class)
public @interface CNPJ {
     String message() default "Formato de CNPJ inválido. Use XX.XXX.XXX/YYYY-ZZ ou apenas números.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
