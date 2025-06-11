package com.group.surveyapp.service.impl;

import com.group.surveyapp.dto.SurveyCreateRequestDto;
import com.group.surveyapp.dto.SurveyUpdateRequestDto;
import com.group.surveyapp.dto.SurveyAnswerRequestDto;
import com.group.surveyapp.dto.SurveyAnswerResponseDto;
import com.group.surveyapp.dto.SurveyResponseDto;
import com.group.surveyapp.service.SurveyService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class SurveyServiceImpl implements SurveyService {

    @Override
    public SurveyResponseDto createSurvey(SurveyCreateRequestDto requestDto) {
        // TODO: 설문 생성 로직 구현 (임시 응답 생성)
        SurveyResponseDto response = new SurveyResponseDto();
        response.setResponseId(1L);
        response.setAnswers(new ArrayList<>());
        response.setCreatedAt("2025-06-10T00:00:00");
        return response;
    }

    @Override
    public SurveyResponseDto updateSurvey(Long surveyId, SurveyUpdateRequestDto requestDto) {
        // TODO: 설문 수정 로직 구현 (임시 응답 생성)
        SurveyResponseDto response = new SurveyResponseDto();
        response.setResponseId(surveyId);
        response.setAnswers(new ArrayList<>());
        response.setCreatedAt("2025-06-10T00:00:00");
        return response;
    }

    @Override
    public void submitAnswer(Long surveyId, SurveyAnswerRequestDto requestDto) {
        // TODO: 설문 응답 저장 로직 구현
        System.out.println("응답 저장 완료 (surveyId=" + surveyId + ")");
    }

    @Override
    public List<SurveyAnswerResponseDto> getAnswers(Long surveyId) {
        // TODO: 설문 응답 조회 로직 구현 (임시 응답 리스트 반환)
        return new ArrayList<>();
    }
}
