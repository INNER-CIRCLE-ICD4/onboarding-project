package survey.survey.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import survey.common.snowflake.Snowflake;
import survey.survey.config.ApplicationException;
import survey.survey.controller.request.SurveyFormCreateRequest;
import survey.survey.controller.request.SurveyFormCreateRequest.QuestionCreateRequest;
import survey.survey.controller.request.SurveyFormUpdateRequest;
import survey.survey.controller.request.SurveyFormUpdateRequest.QuestionUpdateRequest;
import survey.survey.entity.surveyform.SurveyForm;
import survey.survey.entity.surveyquestion.CheckCandidate;
import survey.survey.entity.surveyquestion.SurveyQuestion;
import survey.survey.repository.SurveyFormRepository;
import survey.survey.repository.SurveyQuestionRepository;
import survey.survey.service.response.SurveyFormResponse;
import survey.survey.service.response.SurveyFormUpdateResponse;

import java.util.*;
import java.util.stream.Collectors;

import static survey.survey.config.ErrorType.*;

@Service
@RequiredArgsConstructor
public class SurveyService {
    private static final int MIN_QUESTIONS = 1;
    private static final int MAX_QUESTIONS = 10;

    private final Snowflake surveyFormId = new Snowflake();
    private final Snowflake questionId = new Snowflake();
    private final SurveyFormRepository surveyFormRepository;
    private final SurveyQuestionRepository surveyQuestionRepository;


    @Transactional
    public SurveyFormResponse create(SurveyFormCreateRequest request) {
        SurveyForm surveyForm = createSurveyForm(request);
        SurveyForm savedSurveyForm = surveyFormRepository.save(surveyForm);

        List<SurveyQuestion> questions = createQuestionsFromRequest(request.questionList(), savedSurveyForm.getSurveyFormId());
        validateQuestionsCount(questions);
        List<SurveyQuestion> savedQuestions = surveyQuestionRepository.saveAll(questions);

        return SurveyFormResponse.from(savedSurveyForm, savedQuestions);
    }


    @Transactional
    public SurveyFormUpdateResponse update(SurveyFormUpdateRequest request, Long surveyFormId) {
        SurveyForm surveyForm = findSurveyFormById(surveyFormId);

        boolean formChanged = updateSurveyForm(surveyForm, request);
        boolean questionsChanged = updateSurveyQuestions(surveyFormId, request.questionList());

        if (questionsChanged && !formChanged) {
            surveyForm.incrementVersion();
        }

        List<SurveyQuestion> updatedQuestions = surveyQuestionRepository
                .findSurveyQuestionBySurveyFormIdAndDeletedFalse(surveyFormId);

        return new SurveyFormUpdateResponse(
                surveyFormId,
                formChanged || questionsChanged
        );
    }


    private SurveyForm findSurveyFormById(Long surveyFormId) {
        return surveyFormRepository.findById(surveyFormId)
                .orElseThrow(() -> new ApplicationException(SURVEY_NOT_FOUND));
    }

    private boolean updateSurveyForm(SurveyForm surveyForm, SurveyFormUpdateRequest request) {
        boolean formInfoChanged = !Objects.equals(surveyForm.getTitle(), request.title()) ||
                !Objects.equals(surveyForm.getDescription(), request.description()) ||
                !Objects.equals(surveyForm.getSurveyId(), request.surveyId());

        if (formInfoChanged) {
            surveyForm.update(
                    request.title(),
                    request.description(),
                    request.surveyId());
        }

        return formInfoChanged;
    }

    private boolean updateSurveyQuestions(Long surveyFormId, List<QuestionUpdateRequest> requestQuestions) {
        List<SurveyQuestion> existingQuestions = surveyQuestionRepository.findSurveyQuestionBySurveyFormId(surveyFormId);
        return processQuestionUpdates(existingQuestions, requestQuestions, surveyFormId);
    }

    private boolean processQuestionUpdates(
            List<SurveyQuestion> existingQuestions,
            List<QuestionUpdateRequest> requestQuestions,
            Long surveyFormId) {

        boolean changed = false;
        Map<Integer, SurveyQuestion> existingQuestionMap = mapExistingQuestions(existingQuestions);

        Set<Integer> requestIndices = getRequestIndices(requestQuestions);
        changed |= processQuestionDeletions(existingQuestions, requestIndices);
        changed |= processQuestionUpdatesAndAdditions(existingQuestionMap, requestQuestions, surveyFormId);

        return changed;
    }

    private Map<Integer, SurveyQuestion> mapExistingQuestions(List<SurveyQuestion> existingQuestions) {
        return existingQuestions.stream()
                .filter(q -> !q.isDeleted())
                .collect(Collectors.toMap(SurveyQuestion::getQuestionIndex, q -> q));
    }

    private Set<Integer> getRequestIndices(List<QuestionUpdateRequest> requestQuestions) {
        return requestQuestions.stream()
                .map(QuestionUpdateRequest::questionIndex)
                .collect(Collectors.toSet());
    }

    private boolean processQuestionDeletions(List<SurveyQuestion> existingQuestions, Set<Integer> requestIndices) {
        boolean changed = false;

        for (SurveyQuestion question : existingQuestions) {
            if (!question.isDeleted() && !requestIndices.contains(question.getQuestionIndex())) {
                question.delete();
                changed = true;
            }
        }

        return changed;
    }

    private boolean processQuestionUpdatesAndAdditions(
            Map<Integer, SurveyQuestion> existingQuestionMap,
            List<QuestionUpdateRequest> requestQuestions,
            Long surveyFormId) {

        boolean changed = false;

        for (QuestionUpdateRequest request : requestQuestions) {
            int index = request.questionIndex();

            if (existingQuestionMap.containsKey(index)) {
                boolean questionChanged = updateExistingQuestion(existingQuestionMap.get(index), request);
                changed |= questionChanged;
            } else {
                createAndSaveNewQuestion(surveyFormId, request);
                changed = true;
            }
        }

        return changed;
    }

    private boolean updateExistingQuestion(SurveyQuestion question, QuestionUpdateRequest request) {
        boolean infoChanged = isQuestionInfoChanged(question, request);

        if (infoChanged) {
            question.update(
                    request.name(),
                    request.questionIndex(),
                    request.description(),
                    request.inputType(),
                    request.required()
            );
        }

        boolean candidatesChanged = updateQuestionCandidates(question, request);
        return infoChanged || candidatesChanged;
    }

    private boolean isQuestionInfoChanged(SurveyQuestion question, QuestionUpdateRequest request) {
        return !Objects.equals(question.getName(), request.name()) ||
                question.getQuestionIndex() != request.questionIndex() ||
                !Objects.equals(question.getDescription(), request.description()) ||
                question.getInputType() != request.inputType() ||
                question.isRequired() != request.required();
    }

    private boolean updateQuestionCandidates(SurveyQuestion question, QuestionUpdateRequest request) {
        List<CheckCandidate> candidates = convertToCandidates(request.candidates());

        Long beforeVersion = question.getVersion();
        question.updateCandidates(candidates);

        return !Objects.equals(beforeVersion, question.getVersion());
    }

    private void createAndSaveNewQuestion(Long surveyFormId, QuestionUpdateRequest request) {
        SurveyQuestion newQuestion = SurveyQuestion.create(
                surveyFormId,
                questionId.nextId(),
                request.questionIndex(),
                request.name(),
                request.description(),
                request.inputType(),
                request.required()
        );

        List<CheckCandidate> candidates = convertToCandidates(request.candidates());
        newQuestion.updateCandidates(candidates);

        surveyQuestionRepository.save(newQuestion);
    }

    private List<CheckCandidate> convertToCandidates(List<QuestionUpdateRequest.CandidateUpdateRequest> requests) {
        if (requests == null || requests.isEmpty()) {
            return new ArrayList<>();
        }

        return requests.stream()
                .map(request -> CheckCandidate.of(request.checkCandidateIndex(), request.name()))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private List<CheckCandidate> convertCandidates(List<QuestionCreateRequest.CandidateCreateRequest> candidateRequests) {
        if (candidateRequests == null || candidateRequests.isEmpty()) {
            return new ArrayList<>();
        }

        return candidateRequests.stream()
                .map(request -> CheckCandidate.of(
                        request.checkCandidateIndex(),
                        request.name()
                ))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private SurveyForm createSurveyForm(SurveyFormCreateRequest request) {
        return SurveyForm.create(
                surveyFormId.nextId(),
                request.title(),
                request.description(),
                request.surveyId()
        );
    }

    private List<SurveyQuestion> createQuestionsFromRequest(List<QuestionCreateRequest> questionRequests, Long surveyFormId) {
        return questionRequests.stream()
                .map(request -> createQuestionFromRequest(request, surveyFormId))
                .collect(Collectors.toList());
    }

    private SurveyQuestion createQuestionFromRequest(QuestionCreateRequest request, Long surveyFormId) {
        SurveyQuestion question = SurveyQuestion.create(
                surveyFormId,
                questionId.nextId(),
                request.questionIndex(),
                request.name(),
                request.description(),
                request.inputType(),
                request.required()
        );

        List<CheckCandidate> candidates = convertCandidates(request.candidates());
        question.addCandidates(candidates);

        return question;
    }

    private void validateQuestionsCount(List<SurveyQuestion> questions) {
        long activeQuestionsCount = questions.stream()
                .filter(q -> !q.isDeleted())
                .count();

        if (activeQuestionsCount < MIN_QUESTIONS) {
            throw new ApplicationException(MINIMUM_QUESTION);
        }
        if (activeQuestionsCount > MAX_QUESTIONS) {
            throw new ApplicationException(MAXIMUM_QUESTION);
        }
    }
}