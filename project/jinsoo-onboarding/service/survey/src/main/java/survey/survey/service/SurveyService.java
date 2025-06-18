package survey.survey.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import survey.survey.config.ApplicationException;
import survey.survey.controller.request.survey.create.SurveyCreateRequest;
import survey.survey.controller.request.survey.update.SurveyFormUpdateRequest;
import survey.survey.controller.request.survey.update.SurveyUpdateRequest;
import survey.survey.entity.Survey;
import survey.survey.entity.surveyform.SurveyForm;
import survey.survey.repository.SurveyRepository;
import survey.survey.service.response.SurveyResponse;

import static survey.survey.config.ErrorType.SURVEY_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class SurveyService {
    private final SurveyRepository surveyRepository;
    private final SurveyFormService surveyFormService;
    private final SurveyQuestionService surveyQuestionService;

    /**
     * 새로운 설문을 생성합니다.
     *
     * @param request 설문 생성 요청
     * @return 생성된 설문 응답
     */
    @Transactional
    public SurveyResponse create(SurveyCreateRequest request) {
        Long surveyFormVersion = 1L;

        Survey savedSurvey = Survey.create(surveyFormVersion);

        SurveyForm savedSurveyForm = surveyFormService.create(
                savedSurvey.getId(),
                surveyFormVersion,
                request.surveyFormCreateRequest()
        );

        surveyQuestionService.createQuestionList(
                savedSurveyForm.getId(),
                request.surveyFormCreateRequest().questionList()
        );

        return SurveyResponse.from(surveyRepository.save(savedSurvey));
    }

    /**
     * 설문을 업데이트합니다.
     *
     * @param request 설문 업데이트 요청
     * @return 업데이트된 설문 응답
     */
    @Transactional
    public SurveyResponse update(SurveyUpdateRequest request) {
        Survey survey = findSurveyById(request.surveyId());
        Long oldSurveyFormId = request.surveyFormUpdateRequest().surveyFormId();

        SurveyForm updatedForm = handleFormContentChanges(
                survey.getId(),
                oldSurveyFormId,
                request.surveyFormUpdateRequest()
        );

        Long newSurveyFormId = updatedForm.getId();
        boolean surveyFormChanged = !oldSurveyFormId.equals(newSurveyFormId);

        boolean questionsChanged = handleQuestionChanges(
                survey.getId(),
                oldSurveyFormId,
                newSurveyFormId,
                surveyFormChanged,
                request.surveyFormUpdateRequest()
        );

        if (surveyFormChanged || questionsChanged) {
            survey.increaseVersion(survey.getSurveyFormVersion() + 1);
        }

        return SurveyResponse.from(survey);
    }

    /**
     * 폼 내용 변경을 처리합니다.
     * 
     * @param surveyId 설문 ID
     * @param oldSurveyFormId 이전 설문 폼 ID
     * @param updateRequest 업데이트 요청
     * @return 업데이트된 설문 폼
     */
    private SurveyForm handleFormContentChanges(
            Long surveyId, 
            Long oldSurveyFormId, 
            SurveyFormUpdateRequest updateRequest
    ) {
        boolean formContentChanged = surveyFormService.isFormContentChanged(
                oldSurveyFormId,
                updateRequest.title(),
                updateRequest.description()
        );

        if (formContentChanged) {
            return surveyFormService.update(surveyId, updateRequest);
        } else {
            return surveyFormService.findById(oldSurveyFormId);
        }
    }

    private boolean handleQuestionChanges(
            Long surveyId,
            Long oldSurveyFormId,
            Long newSurveyFormId,
            boolean surveyFormChanged,
            SurveyFormUpdateRequest updateRequest
    ) {
        boolean questionsChanged = false;

        if (surveyFormChanged) {
            questionsChanged = surveyQuestionService.saveQuestionList(
                    newSurveyFormId,
                    oldSurveyFormId,
                    updateRequest.questionList()
            );
        } else {
            questionsChanged = surveyQuestionService.isQuestionsChanged(
                    oldSurveyFormId,
                    updateRequest.questionList()
            );

            if (questionsChanged) {
                SurveyForm updatedForm = surveyFormService.createNewVersion(surveyId, oldSurveyFormId);
                Long updatedFormId = updatedForm.getId();

                surveyQuestionService.saveQuestionList(
                        updatedFormId,
                        oldSurveyFormId,
                        updateRequest.questionList()
                );
            }
        }

        return questionsChanged;
    }

    /**
     * ID로 설문을 조회합니다.
     *
     * @param surveyId 설문 ID
     * @return 조회된 설문
     * @throws ApplicationException 설문이 존재하지 않는 경우
     */
    private Survey findSurveyById(Long surveyId) {
        return surveyRepository.findById(surveyId)
                .orElseThrow(() -> new ApplicationException(SURVEY_NOT_FOUND));
    }
}
