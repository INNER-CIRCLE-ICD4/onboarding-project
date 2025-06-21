package icd.onboarding.surveyproject.survey.service.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = InputTypeValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidInputType {
	String message() default "질문 유형이 유효하지 않습니다.";
	Class<?>[] groups() default {};
	Class<? extends Payload>[] payload() default {};
}
