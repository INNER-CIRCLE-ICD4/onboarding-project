package com.onboarding.api.service;

import com.onboarding.SurveyRepository;
import com.onboarding.api.dto.request.CreateSurveyReq;
import com.onboarding.api.dto.response.CreateSurveyRes;
import com.onboarding.api.dto.response.SearchSurvey;
import com.onboarding.entity.SurveyEntity;
import com.onboarding.mapper.SurveyMapper;
import com.onboarding.model.survey.Survey;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class SurveyService {
    private SurveyRepository surveyRepository;

    public CreateSurveyRes createServey(CreateSurveyReq surveyReq) {
        UUID surveyId = surveyRepository.save(SurveyMapper.toEntity(surveyReq.toDomain())).getId();

        return new CreateSurveyRes(surveyId, "설문지가 작성되었습니다.");
    }

    public SearchSurvey searchSurveyById(String surveyId) {
        SurveyEntity surveyEntity = surveyRepository.findById(UUID.fromString(surveyId))
                    .orElseThrow();

        Survey survey = SurveyMapper.from(surveyEntity);

        return SearchSurvey.from(survey);
    }
}
