package com.survey.choheeonboarding.api.service;

import com.survey.choheeonboarding.api.repository.QuestionOptionRepo;
import com.survey.choheeonboarding.api.repository.SurveyQuestionRepo;
import com.survey.choheeonboarding.api.repository.SurveyRepo;
import com.survey.choheeonboarding.api.dto.SurveyDto;
import com.survey.choheeonboarding.api.entity.QuestionOption;
import com.survey.choheeonboarding.api.entity.Survey;
import com.survey.choheeonboarding.api.entity.SurveyQuestion;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class SurveySvc {

    private final SurveyRepo surveyRepo;
    private final SurveyQuestionRepo surveyQuestionRepo;
    private final QuestionOptionRepo questionOptionRepo;

    public SurveySvc(SurveyRepo surveyRepo, SurveyQuestionRepo surveyQuestionRepo, QuestionOptionRepo questionOptionRepo) {
        this.surveyRepo = surveyRepo;
        this.surveyQuestionRepo = surveyQuestionRepo;
        this.questionOptionRepo = questionOptionRepo;
    }

    @Transactional
    public void createSurvey(SurveyDto.CreateSurveyRequest request) {

        Survey survey = Survey.builder()
                .id(UUID.randomUUID().toString())
                .title(request.title())
                .description(request.description())
                .build();
        surveyRepo.save(survey);

        List<SurveyQuestion> questions = new ArrayList<>();
        List<QuestionOption> questionOptions = new ArrayList<>();

        for (SurveyDto.QuestionDto q : request.questions()) {
            SurveyQuestion question = SurveyQuestion.builder()
                    .id(UUID.randomUUID().toString())
                    .survey(survey)
                    .questionText(q.questionText())
                    .questionDescription(q.questionDescription())
                    .questionType(q.questionType())
                    .isRequired(q.isRequired())
                    .build();
            questions.add(question);

            for (SurveyDto.QuestionOptionDto o : q.options()) {
                QuestionOption option = QuestionOption.builder()
                        .id(UUID.randomUUID().toString())
                        .question(question)
                        .optionText(o.optionText())
                        .build();
                questionOptions.add(option);
            }
        }

        surveyQuestionRepo.saveAll(questions);
        questionOptionRepo.saveAll(questionOptions);


    }

    @Transactional
    public void updateSurvey(SurveyDto.UpdateSurveyRequest request) {

        Survey survey = surveyRepo.findById(request.id())
                .orElseThrow(() -> new IllegalArgumentException("Survey not found"));

        // 1. 기존 설문 업데이트
        survey.setTitle(request.survey().title());
        survey.setDescription(request.survey().description());

        // 2. 기존 질문, 옵션
        List<SurveyQuestion> existingQuestions = surveyQuestionRepo.findBySurveyId(survey.getId());
        List<QuestionOption> existingOptions = questionOptionRepo.findByQuestionIdIn(
                existingQuestions.stream().map(SurveyQuestion::getId).toList()
        );

        // 3. 기존 질문/옵션 삭제 (선택 사항, 전체 교체 방식이면)
        questionOptionRepo.deleteAll(existingOptions);
        surveyQuestionRepo.deleteAll(existingQuestions);

        // 4. 새 질문/옵션 추가
        List<SurveyQuestion> newQuestions = new ArrayList<>();
        List<QuestionOption> newOptions = new ArrayList<>();

        for (SurveyDto.QuestionDto q : request.survey().questions()) {
            SurveyQuestion question = SurveyQuestion.builder()
                    .id(UUID.randomUUID().toString())
                    .survey(survey)
                    .questionText(q.questionText())
                    .questionDescription(q.questionDescription())
                    .questionType(q.questionType())
                    .isRequired(q.isRequired())
                    .build();
            newQuestions.add(question);

            for (SurveyDto.QuestionOptionDto o : q.options()) {
                QuestionOption option = QuestionOption.builder()
                        .id(UUID.randomUUID().toString())
                        .question(question)
                        .optionText(o.optionText())
                        .build();
                newOptions.add(option);
            }
        }

        surveyRepo.save(survey);
        surveyQuestionRepo.saveAll(newQuestions);
        questionOptionRepo.saveAll(newOptions);
    }
}
