package com.example.demo.survey.domain.dto;

import com.example.demo.item.domain.Item;
import com.example.demo.survey.domain.Survey;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SurveyDto {

    private Long surveyId;

    private String name;

    private String description;

    private List<Item> items;

    public static SurveyDto fromSurvey(Survey survey) {
        return SurveyDto.builder()
                .surveyId(survey.getSurveyId())
                .name(survey.getName())
                .description(survey.getDescription())
                .build();
    }
}
