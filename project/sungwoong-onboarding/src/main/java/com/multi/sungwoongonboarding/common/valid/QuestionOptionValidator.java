package com.multi.sungwoongonboarding.common.valid;

import com.multi.sungwoongonboarding.questions.domain.Questions;
import com.multi.sungwoongonboarding.questions.dto.OptionContainer;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

import java.util.List;

import static com.multi.sungwoongonboarding.questions.domain.Questions.QuestionType.*;

public class QuestionOptionValidator implements ConstraintValidator<QuestionOptionValid, OptionContainer> {

    private final Validator validator;

    public QuestionOptionValidator(Validator validator) {
        this.validator = validator;
    }

    @Override
    public void initialize(QuestionOptionValid constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(OptionContainer request, ConstraintValidatorContext constraintValidatorContext) {

        if (request == null) {
            return false;
        }

        String questionTypeText = request.getType();
        List<?> options = request.getOptions();

        // 타입이 null이면 다른 검증에서 처리
        if (questionTypeText == null) {
            return true;
        }

        try {
            Questions.QuestionType questionType = valueOf(questionTypeText);
            if (questionType.isChoiceType()) {
                // 옵션이 null이거나 비어있으면 유효하지 않음
                if (options == null || options.isEmpty()) {
                    constraintValidatorContext.disableDefaultConstraintViolation();
                    constraintValidatorContext.buildConstraintViolationWithTemplate(
                                    "SINGLE_CHOICE 또는 MULTIPLE_CHOICE 타입에서는 옵션 목록이 필수입니다.")
                            .addConstraintViolation();
                    return false;
                }

                // 모든 OptionCreateRequest 검증
                boolean isValid = true;
                for (int i = 0; i < options.size(); i++) {

                    Object optionRequest = options.get(i);
                    var violations = validator.validate(optionRequest);

                    if(!violations.isEmpty()) {
                        isValid = false;
                        // 옵션이 유효하지 않으면 false 반환

                        for (ConstraintViolation<?> violation : violations) {
                            constraintValidatorContext.disableDefaultConstraintViolation();
                            constraintValidatorContext.buildConstraintViolationWithTemplate(violation.getMessage())
                                    .addPropertyNode(String.format("optionCreateRequests[%d]", i))
                                    .addPropertyNode("optionText")
                                    .addConstraintViolation();
                        }
                    }
                }

                return isValid;

            }



            return true;
        } catch (IllegalArgumentException e) {
            return true;
        }
    }
}
