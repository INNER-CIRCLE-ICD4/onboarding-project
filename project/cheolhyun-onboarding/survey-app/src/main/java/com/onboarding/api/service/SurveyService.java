package com.onboarding.api.service;

import com.onboarding.SurveyRepository;
import com.onboarding.api.dto.CreateSurveyReq;
import com.onboarding.mapper.SurveyMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SurveyService {
    private SurveyRepository surveyRepository;

    public void createServey(CreateSurveyReq surveyReq) {
        surveyRepository.save(SurveyMapper.toEntity(surveyReq.toDomain()));
    }
}
