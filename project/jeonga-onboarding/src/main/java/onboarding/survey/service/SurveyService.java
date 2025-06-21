package onboarding.survey.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import onboarding.survey.domain.Survey;
import onboarding.survey.dto.CreateSurveyRequest;
import onboarding.survey.dto.UpdateSurveyRequest;
import onboarding.survey.exception.ApiException;
import onboarding.survey.exception.ErrorCode;
import onboarding.survey.factory.SurveyFactory;
import onboarding.survey.factory.SurveyUpdater;
import onboarding.survey.repository.SurveyRepository;
import org.springframework.stereotype.Service;


@Service
@Transactional
@RequiredArgsConstructor
public class SurveyService {

    private final SurveyRepository surveyRepository;
    private final SurveyFactory surveyFactory;
    private final SurveyUpdater surveyUpdater;


    public Long createSurvey(CreateSurveyRequest request) {
        Survey survey = surveyFactory.createSurvey(request);
        return surveyRepository.save(survey).getId();
    }

    public void updateSurvey(Long id, UpdateSurveyRequest req) {
        Survey survey = surveyRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.RESOURCE_NOT_FOUND, "설문을 찾을 수 없습니다."));
        surveyUpdater.applyUpdates(survey, req);
        surveyRepository.save(survey);
    }
}