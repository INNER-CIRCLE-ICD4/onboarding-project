package fastcampus_inner_circle.junbeom_onboarding.survey.questionary.application.service;

import fastcampus_inner_circle.junbeom_onboarding.survey.questionary.adapter.in.dto.InsertFormRequest;
import fastcampus_inner_circle.junbeom_onboarding.survey.questionary.adapter.in.mapper.SurveyFormMapper;
import fastcampus_inner_circle.junbeom_onboarding.survey.questionary.domain.model.SurveyForm;
import fastcampus_inner_circle.junbeom_onboarding.survey.questionary.domain.repository.SurveyFormRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SurveyFormService {

    private final SurveyFormRepository surveyFormRepository;

    public SurveyForm createSurveyForm(InsertFormRequest insertFormRequest) {
        SurveyForm surveyForm = SurveyFormMapper.toSurveyForm(insertFormRequest);
        return surveyFormRepository.save(surveyForm);
    }

    public SurveyForm updateSurveyForm(Long id, SurveyForm form) {
        // 수정 로직, 검증 등
        // ...
        return surveyFormRepository.save(form);
    }

    // 기타 유스케이스 메서드
} 