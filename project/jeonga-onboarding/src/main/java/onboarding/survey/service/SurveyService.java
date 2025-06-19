package onboarding.survey.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import onboarding.survey.domain.Survey;
import onboarding.survey.dto.CreateSurveyRequest;
import onboarding.survey.factory.SurveyFactory;
import onboarding.survey.repository.SurveyRepository;
import org.springframework.stereotype.Service;


@Service
@Transactional
@RequiredArgsConstructor
public class SurveyService {

    private final SurveyRepository surveyRepository;
    private final SurveyFactory surveyFactory;

    public Long createSurvey(CreateSurveyRequest request) {
        Survey survey = surveyFactory.createSurvey(request);
        return surveyRepository.save(survey).getId();
    }
}