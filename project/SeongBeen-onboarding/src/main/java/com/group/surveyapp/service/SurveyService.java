package com.group.surveyapp.service;

import com.group.surveyapp.dto.request.SurveyCreateRequestDto;
import com.group.surveyapp.dto.request.SurveyUpdateRequestDto;
import com.group.surveyapp.dto.request.SurveyAnswerRequestDto;
import com.group.surveyapp.dto.response.SurveyAnswerResponseDto;
import com.group.surveyapp.dto.response.SurveyResponseDto;

import java.util.List;

public interface SurveyService {
    SurveyResponseDto createSurvey(SurveyCreateRequestDto requestDto);
    SurveyResponseDto updateSurvey(Long surveyId, SurveyUpdateRequestDto requestDto);
    void submitAnswer(Long surveyId, SurveyAnswerRequestDto requestDto);
    List<SurveyAnswerResponseDto> getAnswers(Long surveyId);
}
