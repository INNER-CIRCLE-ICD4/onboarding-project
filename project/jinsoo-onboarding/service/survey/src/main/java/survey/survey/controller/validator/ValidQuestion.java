package survey.survey.controller.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = QuestionTypeValidator.class)
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidQuestion {
    String message() default "질문 유형에 맞지 않는 형식입니다";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

