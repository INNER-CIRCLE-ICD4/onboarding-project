package inner.circle.boram_onboarding.survay.service;

import inner.circle.boram_onboarding.survay.dto.SurveyCreateRequest;
import inner.circle.boram_onboarding.survay.dto.SurveyCreateResponse;
import inner.circle.boram_onboarding.survay.dto.SurveyUpdateRequest;
import inner.circle.boram_onboarding.survay.entity.Question;
import inner.circle.boram_onboarding.survay.entity.Survey;
import inner.circle.boram_onboarding.survay.repository.QuestionRepository;
import inner.circle.boram_onboarding.survay.repository.ResponseRepository;
import inner.circle.boram_onboarding.survay.repository.SurveyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SurveyService {

    private final SurveyRepository surveyRepository;
    private final QuestionRepository questionRepository;
    private final ResponseRepository responseRepository;

    @Transactional
    public SurveyCreateResponse createSurvey(SurveyCreateRequest request) {
        // 1. Survey 엔티티 생성
        Survey survey = new Survey();
        survey.setTitle(request.getTitle());
        survey.setDescription(request.getDescription());

        // 2. Question 리스트 생성 및 매핑
        List<Question> questionList = request.getQuestions().stream()
                .map(q -> {
                    Question question = new Question();
                    question.setSurvey(survey); // 연관관계 주입
                    question.setName(q.getName());
                    question.setDescription(q.getDescription());
                    question.setType(q.getType());
                    question.setRequired(q.getRequired());
                    if (q.getType().isChoice()) {
                        question.setOptions(q.getOptions() != null ?
                                String.join(",", q.getOptions()) : null);
                    }
                    return question;
                }).collect(Collectors.toList());
        survey.setQuestions(questionList);

        // 3. 저장
        surveyRepository.save(survey);

        return new SurveyCreateResponse(survey.getId());
    }

    @Transactional
    public void updateSurvey(Long surveyId, SurveyUpdateRequest request) {

        Survey survey = surveyRepository.findById(surveyId)
                .orElseThrow(() -> new RuntimeException("설문 없음"));

        survey.setTitle(request.getTitle());
        survey.setDescription(request.getDescription());

        // 기존 질문 전체 삭제 (orphanRemoval 옵션을 켜둬야 자동 삭제됨)
        survey.getQuestions().clear();

        // 새로 질문 추가
        List<Question> newQuestions = request.getQuestions().stream()
                .map(q -> {
                    Question question = new Question();
                    question.setSurvey(survey);
                    question.setName(q.getName());
                    question.setDescription(q.getDescription());
                    question.setType(q.getType());
                    question.setRequired(q.getRequired());
                    if (q.getType().isChoice()) {
                        question.setOptions(q.getOptions() != null ?
                                String.join(",", q.getOptions()) : null);
                    }
                    return question;
                }).collect(Collectors.toList());

        survey.getQuestions().addAll(newQuestions);
        // save는 트랜잭션 내에서 자동 처리됨
    }
}
