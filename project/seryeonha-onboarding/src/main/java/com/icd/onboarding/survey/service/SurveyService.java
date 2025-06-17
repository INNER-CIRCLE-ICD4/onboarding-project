package com.icd.onboarding.survey.service;

import com.icd.onboarding.survey.domain.Question;
import com.icd.onboarding.survey.domain.Survey;
import com.icd.onboarding.survey.dto.SurveyDto;
import com.icd.onboarding.survey.repository.OptionRepository;
import com.icd.onboarding.survey.repository.QuestionRepository;
import com.icd.onboarding.survey.repository.SurveyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class SurveyService {
    private final SurveyRepository surveyRepository;
    private final QuestionRepository questionRepository;
    private final OptionRepository optionRepository;

    public void createSurvey(SurveyDto.Create req) {
        log.info("SurveyService.createSurvey req : {}", req);

        Survey survey = Survey.builder()
                .name(req.getName())
                .description(req.getDescription())
                .build();
        surveyRepository.save(survey);

        Long surveyId = survey.getId();
        List<Question> questions = req.getQuestions().stream().map(q -> Question.builder()
                                                                                .surveyId(surveyId)
                                                                                .order(q.getOrder())
                                                                                .name(q.getName())
                                                                                .description(q.getDescription())
                                                                                .type(q.getType())
                                                                                .required(q.isRequired())
                                                                                .active(q.isActive())
                                                                                .build()).toList();

        questionRepository.saveAll(questions);
        // todo option은 어떻게

    }

    public SurveyDto.Read getSurvey(Long id) {
        Survey survey = surveyRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Survey not found"));
        return SurveyDto.Read.fromEntity(survey);
    }

    public void updateSurvey(Long id, SurveyDto.Update req) {
        Survey survey = surveyRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Survey not found"));

    }
}
