package com.va2es.backend.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.FIELD }) // Onde a anotação pode ser usada (aqui, em campos de uma classe)
@Retention(RetentionPolicy.RUNTIME) // Quando a anotação será lida (em tempo de execução)
@Constraint(validatedBy = CPFValidator.class) // Qual classe contém a lógica de validação
public @interface CPF {
    String message() default "Formato de CPF inválido. Use XXX.XXX.XXX-XX ou apenas números.";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
