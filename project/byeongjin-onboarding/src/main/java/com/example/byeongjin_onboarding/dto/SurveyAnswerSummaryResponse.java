package com.example.byeongjin_onboarding.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SurveyAnswerSummaryResponse {
    private Long surveyId;
    private String surveyName;
    private String surveyDescription;
    private long totalSubmissions;
    private List<FormItemAnswerSummaryDto> formItemSummaries;
}