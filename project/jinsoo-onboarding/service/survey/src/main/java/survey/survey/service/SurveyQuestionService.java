package survey.survey.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import survey.survey.config.ApplicationException;
import survey.survey.controller.request.survey.create.CandidateCreateRequest;
import survey.survey.controller.request.survey.create.QuestionCreateRequest;
import survey.survey.controller.request.survey.update.CandidateUpdateRequest;
import survey.survey.controller.request.survey.update.QuestionUpdateRequest;
import survey.survey.entity.surveyquestion.CheckCandidate;
import survey.survey.entity.surveyquestion.SurveyQuestion;
import survey.survey.repository.SurveyQuestionRepository;

import java.util.*;
import java.util.stream.Collectors;

import static survey.survey.config.ErrorType.*;

@Service
@RequiredArgsConstructor
public class SurveyQuestionService {
    private static final int MIN_QUESTIONS = 1;
    private static final int MAX_QUESTIONS = 10;

    private final SurveyQuestionRepository surveyQuestionRepository;

    @Transactional
    public List<SurveyQuestion> createQuestionList(Long surveyFormId,
                                                   List<QuestionCreateRequest> questionRequests
    ) {
        validateNoDuplicateIndices(questionRequests);

        List<SurveyQuestion> surveyQuestionList = questionRequests.stream()
                .map(request -> createQuestionFromRequest(surveyFormId, request))
                .collect(Collectors.toList());
        validateQuestionsCount(surveyQuestionList);
        return surveyQuestionRepository.saveAll(surveyQuestionList);
    }

    @Transactional
    public boolean saveQuestionList(Long newSurveyFormId, Long oldSurveyFormId, List<QuestionUpdateRequest> requests) {
        validateNoDuplicateIndices(requests);

        List<SurveyQuestion> oldQuestions =
                surveyQuestionRepository.findSurveyQuestionBySurveyFormIdAndDeletedFalse(oldSurveyFormId);

        Map<Integer, SurveyQuestion> oldQuestionMap = new HashMap<>();
        for (SurveyQuestion question : oldQuestions) {
            oldQuestionMap.put(question.getQuestionIndex(), question);
        }

        List<SurveyQuestion> newQuestions = new ArrayList<>();
        for (QuestionUpdateRequest request : requests) {
            SurveyQuestion newQuestion = createQuestionFromUpdateRequest(newSurveyFormId, request);
            newQuestions.add(newQuestion);
        }

        validateQuestionsCount(newQuestions);

        boolean questionsChanged = false;

        if (oldQuestions.size() != newQuestions.size()) {
            questionsChanged = true;
        } else {
            for (SurveyQuestion newQuestion : newQuestions) {
                int index = newQuestion.getQuestionIndex();
                SurveyQuestion oldQuestion = oldQuestionMap.get(index);

                if (oldQuestion == null ||
                        !Objects.equals(oldQuestion.getName(), newQuestion.getName()) ||
                        !Objects.equals(oldQuestion.getDescription(), newQuestion.getDescription()) ||
                        !Objects.equals(oldQuestion.getInputType(), newQuestion.getInputType()) ||
                        oldQuestion.isRequired() != newQuestion.isRequired() ||
                        candidatesChanged(oldQuestion.getCandidates(), newQuestion.getCandidates())) {
                    questionsChanged = true;
                    break;
                }
            }
        }

        surveyQuestionRepository.saveAll(newQuestions);
        return questionsChanged;
    }

    private boolean candidatesChanged(List<CheckCandidate> oldCandidates, List<CheckCandidate> newCandidates) {
        if ((oldCandidates == null || oldCandidates.isEmpty()) &&
                (newCandidates == null || newCandidates.isEmpty())) {
            return false;
        }

        if (oldCandidates.size() != newCandidates.size()) {
            return true;
        }

        Map<Integer, CheckCandidate> oldMap = new HashMap<>();
        for (CheckCandidate candidate : oldCandidates) {
            oldMap.put(candidate.getCheckCandidateIndex(), candidate);
        }

        for (CheckCandidate newCandidate : newCandidates) {
            CheckCandidate oldCandidate = oldMap.get(newCandidate.getCheckCandidateIndex());
            if (oldCandidate == null || !Objects.equals(oldCandidate.getName(), newCandidate.getName())) {
                return true;
            }
        }

        return false;
    }

    private SurveyQuestion createQuestionFromRequest(Long surveyFormId, QuestionCreateRequest request) {
        return createQuestion(
                surveyFormId,
                request.questionIndex(),
                request.name(),
                request.description(),
                request.inputType(),
                request.required(),
                request.candidates()
        );
    }


    private SurveyQuestion createQuestionFromUpdateRequest(Long surveyFormId, QuestionUpdateRequest request) {
        return createQuestion(
                surveyFormId,
                request.questionIndex(),
                request.name(),
                request.description(),
                request.inputType(),
                request.required(),
                request.candidates()
        );
    }


    private <T> SurveyQuestion createQuestion(
            Long surveyFormId,
            int questionIndex,
            String name,
            String description,
            survey.survey.entity.surveyquestion.InputType inputType,
            boolean required,
            List<T> candidateRequests
    ) {
        List<CheckCandidate> candidates = new ArrayList<>();
        if (candidateRequests != null) {
            for (var candidate : candidateRequests) {
                int index;
                String candidateName;

                if (candidate instanceof CandidateCreateRequest) {
                    index = ((CandidateCreateRequest) candidate).checkCandidateIndex();
                    candidateName = ((CandidateCreateRequest) candidate).name();
                } else if (candidate instanceof CandidateUpdateRequest) {
                    index = ((CandidateUpdateRequest) candidate).checkCandidateIndex();
                    candidateName = ((CandidateUpdateRequest) candidate).name();
                } else {
                    throw new IllegalArgumentException("Unsupported candidate type: " + candidate.getClass().getName());
                }

                candidates.add(CheckCandidate.of(index, candidateName));
            }
        }

        return SurveyQuestion.create(
                surveyFormId,
                questionIndex,
                name,
                description,
                inputType,
                required,
                candidates
        );
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


    private <T> void validateNoDuplicateIndices(List<T> requests) {
        Set<Integer> indices = new HashSet<>();
        for (T request : requests) {
            int index;
            if (request instanceof QuestionCreateRequest) {
                index = ((QuestionCreateRequest) request).questionIndex();
            } else if (request instanceof QuestionUpdateRequest) {
                index = ((QuestionUpdateRequest) request).questionIndex();
            } else {
                throw new IllegalArgumentException("Unsupported request type: " + request.getClass().getName());
            }

            if (!indices.add(index)) {
                throw new ApplicationException(QUESTION_INDEX_DUPLICATED);
            }
        }
    }

    public boolean isQuestionsChanged(Long surveyFormId, List<QuestionUpdateRequest> requests) {
        validateNoDuplicateIndices(requests);

        List<SurveyQuestion> existingQuestions =
                surveyQuestionRepository.findSurveyQuestionBySurveyFormIdAndDeletedFalse(surveyFormId);

        Map<Integer, SurveyQuestion> existingMap = new HashMap<>();
        for (SurveyQuestion question : existingQuestions) {
            existingMap.put(question.getQuestionIndex(), question);
        }

        if (existingQuestions.size() != requests.size()) {
            return true;
        }

        for (QuestionUpdateRequest request : requests) {
            int index = request.questionIndex();
            SurveyQuestion existingQuestion = existingMap.get(index);

            if (existingQuestion == null ||
                    !Objects.equals(existingQuestion.getName(), request.name()) ||
                    !Objects.equals(existingQuestion.getDescription(), request.description()) ||
                    !Objects.equals(existingQuestion.getInputType(), request.inputType()) ||
                    existingQuestion.isRequired() != request.required()) {
                return true;
            }

            if (request.candidates() != null) {
                List<CheckCandidate> oldCandidates = existingQuestion.getCandidates();

                if ((oldCandidates == null || oldCandidates.isEmpty()) && request.candidates().isEmpty()) {
                    continue;
                }

                if ((oldCandidates == null || oldCandidates.isEmpty()) != request.candidates().isEmpty() ||
                        oldCandidates.size() != request.candidates().size()) {
                    return true;
                }

                Map<Integer, String> oldCandidateMap = new HashMap<>();
                for (CheckCandidate candidate : oldCandidates) {
                    oldCandidateMap.put(candidate.getCheckCandidateIndex(), candidate.getName());
                }

                for (var newCandidate : request.candidates()) {
                    String oldName = oldCandidateMap.get(newCandidate.checkCandidateIndex());
                    if (oldName == null || !oldName.equals(newCandidate.name())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

}
