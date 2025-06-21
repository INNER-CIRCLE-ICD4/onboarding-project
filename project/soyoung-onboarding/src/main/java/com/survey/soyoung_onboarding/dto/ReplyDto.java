package com.survey.soyoung_onboarding.dto;

import com.survey.soyoung_onboarding.entity.Reply;
import com.survey.soyoung_onboarding.service.CommonSerivce;
import lombok.*;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReplyDto {

    private UUID id;
    private String name;
    private String email;
    private String survey_id;
    private Date reg_date;
    private List<AnswerDto> answers;

    public static ReplyDto convert(Reply reply) {
        return ReplyDto.builder()
                .id(reply.getId())
                .name(reply.getName())
                .email(reply.getEmail())
                .reg_date(reply.getReg_date())
                .answers(reply.getAnswers().stream()
                        .map(AnswerDto::convert)
                        .collect(Collectors.toList()))
                .build();
    }

    public void validate(Errors errors) {

        ValidationUtils.rejectIfEmpty(errors, "name", "response.name.empty");

        if (!CommonSerivce.isValidEmail(email)) {
            errors.rejectValue("email", "response.email.invalid", "이메일 형식이 올바르지 않습니다.");
        }

        ValidationUtils.rejectIfEmpty(errors, "survey_id", "response.survey_id.empty");

        if (answers == null || answers.isEmpty()) {
            errors.rejectValue("answers", "response.answers.empty");
            return;
        }

        for (AnswerDto answer : answers) {
            answer.validate(errors); // 각 응답의 유효성 검사
        }
    }
}
