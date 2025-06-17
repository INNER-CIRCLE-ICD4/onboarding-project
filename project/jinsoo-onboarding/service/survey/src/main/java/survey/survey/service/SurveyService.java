package survey.survey.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import survey.common.snowflake.Snowflake;
import survey.survey.controller.request.survey.create.SurveyCreateRequest;
import survey.survey.entity.Survey;
import survey.survey.repository.SurveyRepository;
import survey.survey.service.response.SurveyResponse;

@Service
@RequiredArgsConstructor
public class SurveyService {
    private final Snowflake snowflake = new Snowflake();
    private final SurveyRepository surveyRepository;

    private final SurveyFormService surveyFormService;
    private final SurveyQuestionService surveyQuestionService;

    @Transactional
    public SurveyResponse create(SurveyCreateRequest request) {
        Long surveyFormId = snowflake.nextId();
        Long surveyFormVersion = 1L;

        Survey savedSurvey = Survey.create(
                surveyFormVersion
        );

        surveyFormService.create(savedSurvey.getId(), surveyFormId, surveyFormVersion, request.surveyFormCreateRequest());
        surveyQuestionService.createQuestionList(request.surveyFormCreateRequest().questionList());

        return SurveyResponse.from(surveyRepository.save(savedSurvey));
    }

//    @Transactional
//    public SurveyResponse update(SurveyUpdateRequest request) {
//        Survey survey = findSurveyFormById(request.surveyId());
//
//        surveyFormService.create(survey.getSurveyFormId(), request.surveyFormUpdateRequest());
//
//        return SurveyFormResponse.from(updatedSurveyForm, updatedSurveyQuestionList);
//    }
//
//
//    private Survey findSurveyFormById(Long surveyId) {
//        return surveyRepository.findById(surveyId)
//                .orElseThrow(() -> new ApplicationException(SURVEY_NOT_FOUND));
//    }
//
//    private List<SurveyQuestion> findSurveyQuestionsBySurveyFormId(Long surveyFormId) {
//        return surveyQuestionRepository.findSurveyQuestionBySurveyFormId(surveyFormId);
//    }
//
//    private SurveyForm updateSurveyForm(SurveyForm surveyForm, SurveyFormUpdateRequest request) {
//        boolean formInfoChanged = !Objects.equals(surveyForm.getTitle(), request.title()) ||
//                !Objects.equals(surveyForm.getDescription(), request.description()) ||
//                !Objects.equals(surveyForm.getSurveyId(), request.surveyId());
//
//        if (formInfoChanged) {
//            SurveyForm savedSurveyForm = SurveyForm.create(
//                    snowflake.nextId(),
//                    surveyForm.getVersion(),
//                    request.title(),
//                    request.description(),
//                    request.surveyId()
//            );
//            return surveyFormRepository.save(savedSurveyForm);
//        }
//        return surveyForm;
//    }
//
//    private List<SurveyQuestion> updateSurveyQuestions(Long surveyFormId,
//                                                       Long surveyFormVersion,
//                                                       List<SurveyQuestion> questionList,
//                                                       List<QuestionUpdateRequest> requestQuestions) {
//        return processQuestionUpdates(questionList, requestQuestions, surveyFormId, surveyFormVersion);
//    }
//
//    private List<SurveyQuestion> processQuestionUpdates(
//            List<SurveyQuestion> existingQuestions,
//            List<QuestionUpdateRequest> requestQuestions,
//            Long surveyFormId,
//            Long surveyFormVersion) {
//
//        boolean changed = false;
//        Map<Integer, SurveyQuestion> existingQuestionMap = mapExistingQuestions(existingQuestions);
//
//        Set<Integer> requestIndices = getRequestIndices(requestQuestions);
//        changed |= processQuestionDeletions(existingQuestions, requestIndices);
//
//        return processQuestionUpdatesAndAdditions(existingQuestionMap,
//                requestQuestions, surveyFormId,
//                surveyFormVersion);
//    }
//
//    private Map<Integer, SurveyQuestion> mapExistingQuestions(List<SurveyQuestion> existingQuestions) {
//        return existingQuestions.stream()
//                .filter(q -> !q.isDeleted())
//                .collect(Collectors.toMap(SurveyQuestion::getQuestionIndex, q -> q));
//    }
//
//    private Set<Integer> getRequestIndices(List<QuestionUpdateRequest> requestQuestions) {
//        return requestQuestions.stream()
//                .map(QuestionUpdateRequest::questionIndex)
//                .collect(Collectors.toSet());
//    }
//
//    private boolean processQuestionDeletions(List<SurveyQuestion> existingQuestions, Set<Integer> requestIndices) {
//        boolean changed = false;
//
//        for (SurveyQuestion question : existingQuestions) {
//            if (!question.isDeleted() && !requestIndices.contains(question.getQuestionIndex())) {
//                question.delete();
//                changed = true;
//            }
//        }
//
//        return changed;
//    }
//
//    private List<SurveyQuestion> processQuestionUpdatesAndAdditions(
//            Map<Integer, SurveyQuestion> existingQuestionMap,
//            List<QuestionUpdateRequest> requestQuestions,
//            Long surveyFormId,
//            Long surveyFormVersion) {
//
//        List<SurveyQuestion> updatedQuestions = new ArrayList<>();
//        for (QuestionUpdateRequest request : requestQuestions) {
//            int index = request.questionIndex();
//
//            if (existingQuestionMap.containsKey(index)) {
//                SurveyQuestion questionChanged = updateExistingQuestion(existingQuestionMap.get(index), request);
//                updatedQuestions.add(questionChanged);
//            } else {
//                SurveyQuestion andSaveNewQuestion = createAndSaveNewQuestion(surveyFormId, surveyFormVersion, request);
//                updatedQuestions.add(andSaveNewQuestion);
//            }
//        }
//        return updatedQuestions;
//    }
//
//    private SurveyQuestion updateExistingQuestion(SurveyQuestion question, QuestionUpdateRequest request) {
//        boolean infoChanged = isQuestionInfoChanged(question, request);
//        if (infoChanged) {
//            SurveyQuestion surveyQuestion = SurveyQuestion.create(
//                    question.getSurveyFormId(),
//                    question.getVersion(),
//                    snowflake.nextId(),
//                    request.questionIndex(),
//                    request.name(),
//                    request.description(),
//                    request.inputType(),
//                    request.required()
//            );
//            updateQuestionCandidates(surveyQuestion, request);
//            return surveyQuestionRepository.save(surveyQuestion);
//        }
//        return question;
//    }
//
//    private boolean isQuestionInfoChanged(SurveyQuestion question, QuestionUpdateRequest request) {
//        return !Objects.equals(question.getName(), request.name()) ||
//                question.getQuestionIndex() != request.questionIndex() ||
//                !Objects.equals(question.getDescription(), request.description()) ||
//                question.getInputType() != request.inputType() ||
//                question.isRequired() != request.required();
//    }
//
//    private boolean updateQuestionCandidates(SurveyQuestion question, QuestionUpdateRequest request) {
//        List<CheckCandidate> candidates = convertToCandidates(request.candidates());
//
//        Long beforeVersion = question.getVersion();
//        question.updateCandidates(candidates);
//
//        return !Objects.equals(beforeVersion, question.getVersion());
//    }
//
//    private SurveyQuestion createAndSaveNewQuestion(Long surveyFormId, Long surveyFormVersion, QuestionUpdateRequest request) {
//        SurveyQuestion newQuestion = SurveyQuestion.create(
//                surveyFormId,
//                surveyFormVersion,
//                snowflake.nextId(),
//                request.questionIndex(),
//                request.name(),
//                request.description(),
//                request.inputType(),
//                request.required()
//        );
//
//        List<CheckCandidate> candidates = convertToCandidates(request.candidates());
//        newQuestion.updateCandidates(candidates);
//
//        return surveyQuestionRepository.save(newQuestion);
//    }
//
//    private List<CheckCandidate> convertToCandidates(List<QuestionUpdateRequest.CandidateUpdateRequest> requests) {
//        if (requests == null || requests.isEmpty()) {
//            return new ArrayList<>();
//        }
//
//        return requests.stream()
//                .map(request -> CheckCandidate.of(request.checkCandidateIndex(), request.name()))
//                .collect(Collectors.toCollection(ArrayList::new));
//    }
}