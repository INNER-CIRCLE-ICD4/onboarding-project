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
}
