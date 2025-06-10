package com.icd.onboarding.survey.service;

import com.icd.onboarding.survey.domain.Survey;
import com.icd.onboarding.survey.dto.SurveyDto;
import com.icd.onboarding.survey.repository.SurveyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class SurveyService {
    private final SurveyRepository surveyRepository;

    public void createSurvey(SurveyDto.Create req) {
        log.info("SurveyService.createSurvey req : {}", req);
        surveyRepository.save(req.toEntity());
    }

    public SurveyDto.Read getSurvey(Long id) {
        Survey survey = surveyRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Survey not found"));
        return SurveyDto.Read.fromEntity(survey);
    }

    public void updateSurvey(Long id, SurveyDto.Update req) {
        Survey survey = surveyRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Survey not found"));

    }
}
