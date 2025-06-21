package com.onboarding.api.dto.response;

import com.onboarding.api.dto.mapper.SurveyDtoMapper;
import com.onboarding.model.survey.Survey;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class SearchSurvey {
    private String serveyId;
    private String title;
    private String description;
    private List<QuestionRes> items;

    public SearchSurvey(String serveyId, String title, String description, List<QuestionRes> items) {
        this.serveyId = serveyId;
        this.title = title;
        this.description = description;
        this.items = items;
    }

    public static SearchSurvey from(Survey survey) {
        List<QuestionRes> questionResList = survey.getQuestions().getQuestions().stream()
                    .map(SurveyDtoMapper::from)
                .collect(Collectors.toList());

        return new SearchSurvey(
                survey.getId(),
                survey.getTitle(),
                survey.getDescription(),
                questionResList
        );
    }
}
