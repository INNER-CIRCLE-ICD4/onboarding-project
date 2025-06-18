package survey.surveyresponse.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import survey.common.exception.ApiException;
import survey.common.exception.ErrorType;
import survey.surveyresponse.client.SurveyClient;
import survey.surveyresponse.config.ApplicationException;
import survey.surveyresponse.controller.request.SurveySubmitRequest;
import survey.surveyresponse.entity.SurveyQuestionSubmit;
import survey.surveyresponse.entity.SurveySubmit;
import survey.surveyresponse.repository.SurveyQuestionSubmitRepository;
import survey.surveyresponse.repository.SurveySubmitRepository;
import survey.surveyresponse.service.response.SurveyQuestionSubmitResponse;
import survey.surveyresponse.service.response.SurveySubmitResponse;

import java.util.ArrayList;
import java.util.List;

import static survey.common.exception.ErrorType.RESOURCE_VERSION_MISMATCH;
import static survey.surveyresponse.config.ErrorType.SURVEY_NOT_CURRENT_FORM;

@Service
@RequiredArgsConstructor
public class SurveySubmitService {
    private final SurveyClient surveyClient;

    private final SurveySubmitRepository surveySubmitRepository;
    private final SurveyQuestionSubmitRepository surveyQuestionSubmitRepository;

    @Transactional
    public SurveySubmitResponse submit(Long surveyId, SurveySubmitRequest surveySubmitRequest) {
        Long surveySubmitId = surveySubmitRepository.save(SurveySubmit.create(surveyId, surveySubmitRequest.surveyFormId())).getId();
        List<SurveyQuestionSubmit> surveyQuestionSubmitList =
                surveySubmitRequest.surveyQuestionSubmitRequests().stream()
                        .map(request -> SurveyQuestionSubmit.create(
                                request.surveyQuestionId(), request.answer(), surveySubmitId
                        )).toList();

        isCurrentSurveyForm(surveyId, surveySubmitRequest);

        List<SurveyQuestionSubmit> savedSurveyQuestionSubmitList = surveyQuestionSubmitRepository.saveAll(surveyQuestionSubmitList);
        List<SurveyQuestionSubmitResponse> surveyQuestionSubmitResponseList =
                savedSurveyQuestionSubmitList.stream().map(SurveyQuestionSubmitResponse::of).toList();

        return SurveySubmitResponse.of(surveySubmitId, surveyQuestionSubmitResponseList);
    }

    private void isCurrentSurveyForm(Long surveyId, SurveySubmitRequest surveySubmitRequest) {
        Long surveyFormId = surveyClient.fetchSurveyFormId(surveyId);
        if (!surveySubmitRequest.surveyFormId().equals(surveyFormId))
            throw new ApplicationException(SURVEY_NOT_CURRENT_FORM);
    }
}
