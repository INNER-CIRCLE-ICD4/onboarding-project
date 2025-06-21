package com.onboarding.api.dto;

import com.onboarding.model.survey.Question;
import com.onboarding.model.survey.Questions;
import com.onboarding.model.survey.Survey;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;


@Data
public class CreateSurveyReq
{
    private String title;
    private String description;
    private List<QuestionReq> items;

    public Survey toDomain() {
        List<Question> questionList = items.stream()
                .map(QuestionReq::toDomain)
                .collect(Collectors.toList());

        return Survey.builder()
                .title(title)
                .description(description)
                .questions(new Questions(questionList))
                .build();
    }
}
