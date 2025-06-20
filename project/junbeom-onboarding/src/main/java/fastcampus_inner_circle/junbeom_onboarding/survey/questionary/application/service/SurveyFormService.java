package fastcampus_inner_circle.junbeom_onboarding.survey.questionary.application.service;

import fastcampus_inner_circle.junbeom_onboarding.survey.questionary.adapter.in.dto.InsertFormRequest;
import fastcampus_inner_circle.junbeom_onboarding.survey.questionary.adapter.in.dto.UpdateFormRequest;
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

    public SurveyForm updateSurveyForm(Long id, UpdateFormRequest request) {
        // 1. 기존 설문 폼 조회
        SurveyForm form = surveyFormRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("설문 폼이 존재하지 않습니다."));

        // 2. 이름/설명 수정
        form.updateNameAndDescribe(request.getName(), request.getDescribe());

        // 3. 문항/옵션 수정
        form.updateContents(request.getContents());

        // 4. 저장 후 반환
        return surveyFormRepository.save(form);
    }

    // 기타 유스케이스 메서드
} 