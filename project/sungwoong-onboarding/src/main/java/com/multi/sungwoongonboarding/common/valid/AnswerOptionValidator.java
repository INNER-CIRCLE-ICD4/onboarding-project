package com.multi.sungwoongonboarding.common.valid;

import com.multi.sungwoongonboarding.questions.application.repository.QuestionRepository;
import com.multi.sungwoongonboarding.questions.domain.Questions;
import com.multi.sungwoongonboarding.submission.dto.AnswerCreateRequest;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AnswerOptionValidator implements ConstraintValidator<AnswerOptionValid, AnswerCreateRequest> {

    private final QuestionRepository questionRepository;

    @Override
    public void initialize(AnswerOptionValid constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(AnswerCreateRequest answerCreateRequest, ConstraintValidatorContext constraintValidatorContext) {

        if (answerCreateRequest.getQuestionId() == null) {
            constraintValidatorContext.buildConstraintViolationWithTemplate("유효하지 않은 답변 요청입니다.")
                    .addPropertyNode("questionId")
                    .addConstraintViolation();
        }

        Questions question = questionRepository.findById(answerCreateRequest.getQuestionId());

        // 필수가 아니면 넘어감.
        if (!question.isRequired()) {
            return true; // If the question is not required, no validation needed
        }

        // 질문이 선택 유형이라면 옵션_id가 필요하다.
        // 질문이 선택 유형이라면 해당 질문의 옵션인지 검증한다.
        boolean isValid = true;
        if (question.getQuestionType().isChoiceType()) {

            if (answerCreateRequest.getOptionId() == null) {
                makeFieldError(constraintValidatorContext, "선택유형의 질문은 옵션이 필요합니다.", "optionId");
                isValid = false;
            }

            boolean isValidOption = question.getOptions().stream().anyMatch(option -> option.getId().equals(answerCreateRequest.getOptionId()));

            if (!isValidOption) {
                makeFieldError(constraintValidatorContext, "유효하지 않은 option_id 입니다.", "optionId");
                isValid = false;
            }

        } else {

            if (answerCreateRequest.getAnswerText() == null || answerCreateRequest.getAnswerText().isBlank()) {
                makeFieldError(constraintValidatorContext, "텍스트형 질문에는 answer_text 가 필수입니다.", "answerText");
                isValid = false;
            }
        }
        return isValid;
    }

    private static void makeFieldError(ConstraintValidatorContext constraintValidatorContext, String s, String optionId) {
        constraintValidatorContext.disableDefaultConstraintViolation();
        constraintValidatorContext.buildConstraintViolationWithTemplate(s)
                .addPropertyNode(optionId)
                .addConstraintViolation();
    }
}
