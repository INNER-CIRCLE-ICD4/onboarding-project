package icd.onboarding.surveyproject.survey.service.validator;

import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidInputType {
	String message() default "입력 유형이 유효하지 않습니다.";
	Class<?>[] group() default {};
	Class<? extends Payload>[] payload() default {};
}
