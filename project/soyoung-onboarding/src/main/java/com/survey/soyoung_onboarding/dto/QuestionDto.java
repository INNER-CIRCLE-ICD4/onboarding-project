package com.survey.soyoung_onboarding.dto;

import com.survey.soyoung_onboarding.domain.QuestionType;
import lombok.Getter;
import lombok.Setter;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

import java.util.List;
import java.util.Set;

@Getter
@Setter
public class QuestionDto {

    private static final Set<QuestionType> VALID_QUESTION_TYPE = Set.of(
            QuestionType.SINGLE,
            QuestionType.MULTIPLE,
            QuestionType.SHORT,
            QuestionType.LONG
    );

    private String survey_id;
    private Long id;
    private String title;
    private QuestionType type;
    private boolean required = false;
    private List<String> options;

    public void validate_add(Errors errors) {

        ValidationUtils.rejectIfEmpty(errors, "title", "question.title.empty");

        if (!VALID_QUESTION_TYPE.contains(this.type)) {
            errors.rejectValue("type", "question.type.invalid");
        }

        boolean need_options = type == QuestionType.SINGLE || type == QuestionType.MULTIPLE;
        boolean option_empty = options == null || options.isEmpty();
        if ( need_options && option_empty ) {
            ValidationUtils.rejectIfEmpty(errors, "options", "question.options.empty");
        }
    }

}
