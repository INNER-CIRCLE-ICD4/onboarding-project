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
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class SurveyService {
    private final SurveyRepository surveyRepository;
    private final QuestionRepository questionRepository;
    private final OptionRepository optionRepository;

    public void createSurvey(SurveyDto.Create req) {
        log.info("SurveyService.createSurvey req : {}", req);
//        Survey survey = surveyRepository.save(req.toEntity());
//        Long surveyId = survey.getId();

//        List<Question> questions = req.getQuestions().stream().map(question -> question.toEntity()).collect(Collectors.toList());

//        questionRepository.saveAll()
    }

    public SurveyDto.Read getSurvey(Long id) {
        Survey survey = surveyRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Survey not found"));
        return SurveyDto.Read.fromEntity(survey);
    }

    public void updateSurvey(Long id, SurveyDto.Update req) {
        Survey survey = surveyRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Survey not found"));

    }
}
