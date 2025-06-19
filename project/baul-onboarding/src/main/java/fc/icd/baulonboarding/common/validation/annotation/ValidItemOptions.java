package fc.icd.baulonboarding.common.validation.annotation;


import fc.icd.baulonboarding.common.validation.validator.GenericItemOptionValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = GenericItemOptionValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidItemOptions {
    String message() default "...";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};

    String inputTypeField();
    String optionListField();
}
