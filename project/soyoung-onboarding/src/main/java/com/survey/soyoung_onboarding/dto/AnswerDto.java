package com.survey.soyoung_onboarding.dto;

import com.survey.soyoung_onboarding.entity.Answer;
import com.survey.soyoung_onboarding.entity.QuestionOption;
import lombok.*;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AnswerDto {

    private Long id;
    private Long question_id;
    private String answer_text;
    private List<Long> selected_option_ids;

    public static AnswerDto convert(Answer answer) {
        return AnswerDto.builder()
                .id(answer.getId())
                .question_id(answer.getQuestion().getId())
                .answer_text(answer.getAnswerText())
                .selected_option_ids(
                        Optional.ofNullable(answer.getSelected_options())
                                .orElse(Collections.emptyList())
                                .stream()
                                .flatMap(answerOption ->
                                        Optional.ofNullable(answerOption.getQuestion_options())
                                                .orElse(Collections.emptyList())
                                                .stream()
                                                .map(QuestionOption::getId)
                                )
                                .collect(Collectors.toList())
                )
                .build();
    }

    public void validate(Errors errors) {

        ValidationUtils.rejectIfEmpty(errors, "question_id", "answer.question_id.empty");

        boolean isTextEmpty = answer_text == null || answer_text.trim().isEmpty();
        boolean isOptionsEmpty = selected_option_ids == null || selected_option_ids.isEmpty();

        if (isTextEmpty && isOptionsEmpty) {
            errors.reject("answer_value.missing", "응답값이 존재하지 않습니다.");
        }
    }

}
