package com.group.surveyapp.service;

import com.group.surveyapp.dto.SurveyCreateRequestDto;
import com.group.surveyapp.dto.SurveyUpdateRequestDto;
import com.group.surveyapp.dto.SurveyAnswerRequestDto;
import com.group.surveyapp.dto.SurveyAnswerResponseDto;
import com.group.surveyapp.dto.SurveyResponseDto;

import java.util.List;

public interface SurveyService {
    SurveyResponseDto createSurvey(SurveyCreateRequestDto requestDto);
    SurveyResponseDto updateSurvey(Long surveyId, SurveyUpdateRequestDto requestDto);
    void submitAnswer(Long surveyId, SurveyAnswerRequestDto requestDto);
    List<SurveyAnswerResponseDto> getAnswers(Long surveyId);
}
