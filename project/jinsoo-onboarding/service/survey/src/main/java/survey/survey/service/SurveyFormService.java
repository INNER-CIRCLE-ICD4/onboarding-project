package survey.survey.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import survey.common.snowflake.Snowflake;
import survey.survey.config.ApplicationException;
import survey.survey.controller.request.SurveyFormCreateRequest;
import survey.survey.controller.request.SurveyFormCreateRequest.QuestionCreateRequest;
import survey.survey.entity.surveyform.SurveyForm;
import survey.survey.entity.surveyform.SurveyFormId;
import survey.survey.entity.surveyquestion.CheckCandidate;
import survey.survey.entity.surveyquestion.SurveyQuestion;
import survey.survey.repository.SurveyFormRepository;
import survey.survey.service.response.SurveyFormResponse;

import java.util.List;
import java.util.stream.Collectors;

import static survey.survey.config.ErrorType.MAXIMUM_QUESTION;
import static survey.survey.config.ErrorType.MINIMUM_QUESTION;

@Service
@RequiredArgsConstructor
public class SurveyFormService {
    private static final int MIN_QUESTIONS = 1;
    private static final int MAX_QUESTIONS = 10;

    private final Snowflake surveyFormId = new Snowflake();
    private final Snowflake questionId = new Snowflake();
    private final SurveyFormRepository surveyFormRepository;

    @Transactional
    public SurveyFormResponse create(SurveyFormCreateRequest request) {
        SurveyForm surveyForm = createSurveyForm(request);
        List<SurveyQuestion> questions = createQuestionsFromRequest(request.getQuestionList());
        validateQuestionsCount(questions);
        surveyForm.addAllQuestions(questions);

        return SurveyFormResponse.from(surveyFormRepository.save(surveyForm));
    }

    private SurveyForm createSurveyForm(SurveyFormCreateRequest request) {
        return SurveyForm.create(
                SurveyFormId.create(surveyFormId.nextId()),
                request.getTitle(),
                request.getDescription(),
                request.getSurveyId()
        );
    }

    private List<SurveyQuestion> createQuestionsFromRequest(List<QuestionCreateRequest> questionRequests) {
        return questionRequests.stream()
                .map(this::createQuestionFromRequest)
                .collect(Collectors.toList());
    }

    private SurveyQuestion createQuestionFromRequest(QuestionCreateRequest request) {
        SurveyQuestion question = SurveyQuestion.create(
                questionId.nextId(),
                request.getQuestionIndex(),
                request.getName(),
                request.getDescription(),
                request.getInputType(),
                request.isRequired()
        );

        List<CheckCandidate> candidates = convertCandidates(request.getCandidates());
        question.addCandidate(candidates);

        return question;
    }

    private List<CheckCandidate> convertCandidates(
            List<QuestionCreateRequest.CandidateCreateRequest> candidateRequests) {
        if (candidateRequests == null || candidateRequests.isEmpty()) {
            return List.of();
        }

        return candidateRequests.stream()
                .map(request -> CheckCandidate.of(
                        request.getCheckCandidateIndex(),
                        request.getName()
                ))
                .collect(Collectors.toList());
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