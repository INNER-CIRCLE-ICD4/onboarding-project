package onboarding.survey.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import onboarding.survey.domain.Survey;
import onboarding.survey.domain.SurveyResponse;
import onboarding.survey.exception.ApiException;
import onboarding.survey.exception.ErrorCode;
import onboarding.survey.repository.SurveyRepository;
import onboarding.survey.repository.SurveyResponseRepository;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class ResponseService {
    private final SurveyRepository surveyRepository;
    private final SurveyResponseRepository responseRepository;

    @Transactional
    public SurveyResponse submit(Long surveyId, Long surveyItemId, String answer) {
        Survey survey = findSurvey(surveyId);
        validateItemExists(survey, surveyItemId);
        SurveyResponse response = createResponse(surveyId, surveyItemId, answer);
        return saveResponse(response);
    }

    private Survey findSurvey(Long surveyId) {
        return surveyRepository.findById(surveyId)
                .orElseThrow(() -> new ApiException(
                        ErrorCode.RESOURCE_NOT_FOUND,
                        "설문을 찾을 수 없습니다. (id=" + surveyId + ")"
                ));
    }

    private void validateItemExists(Survey survey, Long itemId) {
        boolean exists = survey.getItems().stream()
                .anyMatch(item -> item.getId().equals(itemId));
        if (!exists) {
            throw new ApiException(
                    ErrorCode.RESOURCE_NOT_FOUND,
                    "해당 문항을 찾을 수 없습니다. (id=" + itemId + ")"
            );
        }
    }

    private SurveyResponse createResponse(Long surveyId, Long itemId, String answer) {
        return new SurveyResponse(surveyId, itemId, answer);
    }

    private SurveyResponse saveResponse(SurveyResponse response) {
        return responseRepository.save(response);
    }
}