package com.INNER_CIRCLE_ICD4.innerCircle.mapper;


import com.INNER_CIRCLE_ICD4.innerCircle.domain.*;
import com.INNER_CIRCLE_ICD4.innerCircle.dto.*;

import java.util.List;

public class SurveyMapper {

    public static SurveyResponse toResponse(Survey survey) {
        List<QuestionResponse> questionResponses = survey.getQuestions().stream()
                .map(q -> new QuestionResponse(
                        q.getId(),
                        q.getTitle(),
                        q.getDescription(),
                        q.getType(),
                        q.isRequired(),
                        q.getChoices().stream()
                                .map(c -> new ChoiceResponse(
                                        c.getId(),
                                        c.getText(),
                                        c.getChoiceIndex()
                                ))
                                .toList()
                ))
                .toList();

        return new SurveyResponse(
                survey.getId(),
                survey.getTitle(),
                survey.getDescription(),
                survey.getVersion(),
                questionResponses
        );
    }
}