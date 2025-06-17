package survey.survey.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import survey.survey.config.ApplicationException;
import survey.survey.controller.request.survey.create.CandidateCreateRequest;
import survey.survey.controller.request.survey.create.QuestionCreateRequest;
import survey.survey.entity.surveyquestion.CheckCandidate;
import survey.survey.entity.surveyquestion.SurveyQuestion;
import survey.survey.repository.SurveyQuestionRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static survey.survey.config.ErrorType.MAXIMUM_QUESTION;
import static survey.survey.config.ErrorType.MINIMUM_QUESTION;

@Service
@RequiredArgsConstructor
public class SurveyQuestionService {
    private static final int MIN_QUESTIONS = 1;
    private static final int MAX_QUESTIONS = 10;

    private final SurveyQuestionRepository surveyQuestionRepository;

    public List<SurveyQuestion> createQuestionList(
                                                    List<QuestionCreateRequest> questionRequests
    ) {
        List<SurveyQuestion> surveyQuestionList = questionRequests.stream()
                .map(this::createQuestionFromRequest)
                .collect(Collectors.toList());
        validateQuestionsCount(surveyQuestionList);
        return surveyQuestionRepository.saveAll(surveyQuestionList);
    }

    private SurveyQuestion createQuestionFromRequest(QuestionCreateRequest request) {
        SurveyQuestion question = SurveyQuestion.create(
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

    private List<CheckCandidate> convertCandidates(List<CandidateCreateRequest> candidateRequests) {
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
