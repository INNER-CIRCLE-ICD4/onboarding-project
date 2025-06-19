package survey.surveyresponse.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import survey.surveyresponse.client.SurveyClient;
import survey.surveyresponse.client.SurveyReadClient;
import survey.surveyresponse.config.ApplicationException;
import survey.surveyresponse.controller.request.SurveySubmitRequest;
import survey.surveyresponse.entity.SurveyQuestionSubmit;
import survey.surveyresponse.entity.SurveySubmit;
import survey.surveyresponse.repository.SurveyQuestionSubmitRepository;
import survey.surveyresponse.repository.SurveySubmitRepository;
import survey.surveyresponse.service.response.SurveyQuestionSubmitResponse;
import survey.surveyresponse.service.response.surveyresponsequery.QuestionResponse;
import survey.surveyresponse.service.response.surveyresponsequery.SurveyResponse;

import java.util.List;

import static survey.surveyresponse.config.ErrorType.SURVEY_NOT_CURRENT_FORM;
@Slf4j
@Service
@RequiredArgsConstructor
public class SurveyResponseService {
    private final SurveyClient surveyClient;
    private final SurveyReadClient surveyReadClient;
    private final SurveySubmitRepository surveySubmitRepository;
    private final SurveyQuestionSubmitRepository surveyQuestionSubmitRepository;

    @Transactional
    public survey.surveyresponse.service.response.SurveySubmitResponse submit(Long surveyId, SurveySubmitRequest surveySubmitRequest) {
        Long surveySubmitId = surveySubmitRepository.save(SurveySubmit.create(surveyId, surveySubmitRequest.surveyFormId())).getId();
        List<SurveyQuestionSubmit> surveyQuestionSubmitList =
                surveySubmitRequest.surveyQuestionSubmitRequests().stream()
                        .map(request -> SurveyQuestionSubmit.create(
                                request.surveyQuestionId(), request.answer(), surveyId, surveySubmitRequest.surveyFormId(), surveySubmitId
                        )).toList();

        isCurrentSurveyForm(surveyId, surveySubmitRequest);

        List<SurveyQuestionSubmit> savedSurveyQuestionSubmitList = surveyQuestionSubmitRepository.saveAll(surveyQuestionSubmitList);
        List<SurveyQuestionSubmitResponse> surveyQuestionSubmitResponseList =
                savedSurveyQuestionSubmitList.stream().map(SurveyQuestionSubmitResponse::of).toList();

        sendToReadModule(surveyId, surveySubmitRequest.surveyFormId(), surveySubmitId, savedSurveyQuestionSubmitList);
        return survey.surveyresponse.service.response.SurveySubmitResponse.of(surveySubmitId, surveyQuestionSubmitResponseList);
    }

    private void isCurrentSurveyForm(Long surveyId, SurveySubmitRequest surveySubmitRequest) {
        Long surveyFormId = surveyClient.fetchSurveyFormId(surveyId);
        if (!surveySubmitRequest.surveyFormId().equals(surveyFormId))
            throw new ApplicationException(SURVEY_NOT_CURRENT_FORM);
    }


    private void sendToReadModule(Long surveyId, Long surveyFormId, Long surveySubmitId, List<SurveyQuestionSubmit> questionSubmits) {
        try {
            // 질문 응답 목록 구성
            List<QuestionResponse> questionResponses = questionSubmits.stream()
                    .map(q -> new QuestionResponse(
                            q.getQuestionId(),
                            q.getSurveySubmitId(),
                            q.getAnswer()
                    )).toList();

            // 읽기 모델용 DTO 생성
            SurveyResponse response = new SurveyResponse(
                    surveyId,
                    surveyFormId,
                    surveySubmitId,
                    questionResponses
            );

            // 비동기로 survey-read 모듈에 전송
            surveyReadClient.saveResponse(response);
        } catch (Exception e) {
            // 로깅만 하고 주 비즈니스 로직은 실패하지 않도록 함
            // 실제 환경에서는 재시도 로직이나 이벤트 큐에 저장하는 것이 좋음
            log.error("Failed to send response to read module: {}", e.getMessage());
        }
    }

}
