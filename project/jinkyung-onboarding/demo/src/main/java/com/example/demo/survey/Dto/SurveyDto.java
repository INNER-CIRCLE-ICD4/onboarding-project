package com.example.demo.survey.Dto;

import com.example.demo.item.Item;
import com.example.demo.survey.Survey;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
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
