package com.multi.sungwoongonboarding.common.valid;

import com.multi.sungwoongonboarding.options.dto.OptionCreateRequest;
import com.multi.sungwoongonboarding.questions.domain.Questions;
import com.multi.sungwoongonboarding.questions.dto.QuestionCreateRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

import java.util.List;

import static com.multi.sungwoongonboarding.questions.domain.Questions.QuestionType.*;

public class OptionValidator implements ConstraintValidator<OptionValid, QuestionCreateRequest> {

    private final Validator validator;

    public OptionValidator(Validator validator) {
        this.validator = validator;
    }

    @Override
    public void initialize(OptionValid constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(QuestionCreateRequest questionCreateRequest, ConstraintValidatorContext constraintValidatorContext) {

        if (questionCreateRequest == null) {
            return false;
        }

        String questionType = questionCreateRequest.getQuestionType();
        List<OptionCreateRequest> options = questionCreateRequest.getOptionCreateRequests();

        // 타입이 null이면 다른 검증에서 처리
        if (questionType == null) {
            return true;
        }

        try {
            Questions.QuestionType type = valueOf(questionType);
            if (type == SINGLE_CHOICE || type == MULTIPLE_CHOICE) {
                // 옵션이 null이거나 비어있으면 유효하지 않음
                if (options == null || options.isEmpty()) {
                    return false;
                }


                // 모든 OptionCreateRequest 검증
                boolean isValid = true;
                for (int i = 0; i < options.size(); i++) {

                    OptionCreateRequest optionCreateRequest = options.get(i);
                    var violations = validator.validate(optionCreateRequest);

                    if(!violations.isEmpty()) {
                        isValid = false;
                        // 옵션이 유효하지 않으면 false 반환

                        for (ConstraintViolation<OptionCreateRequest> violation : violations) {
                            constraintValidatorContext.disableDefaultConstraintViolation();
                            constraintValidatorContext.buildConstraintViolationWithTemplate(violation.getMessage())
                                    .addPropertyNode("optionCreateRequests[" + i + "]")
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
