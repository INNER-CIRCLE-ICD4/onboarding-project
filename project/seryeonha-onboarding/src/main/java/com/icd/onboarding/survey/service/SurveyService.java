package com.icd.onboarding.survey.service;

import com.icd.onboarding.survey.domain.Option;
import com.icd.onboarding.survey.domain.Question;
import com.icd.onboarding.survey.domain.Survey;
import com.icd.onboarding.survey.dto.OptionDto;
import com.icd.onboarding.survey.dto.QuestionDto;
import com.icd.onboarding.survey.dto.SurveyDto;
import com.icd.onboarding.survey.repository.OptionRepository;
import com.icd.onboarding.survey.repository.QuestionRepository;
import com.icd.onboarding.survey.repository.SurveyRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class SurveyService {
    private final SurveyRepository surveyRepository;
    private final QuestionRepository questionRepository;
    private final OptionRepository optionRepository;

    @Transactional
    public void createSurvey(SurveyDto.Create req) {
        log.info("SurveyService.createSurvey req : {}", req);

        Survey survey = surveyRepository.save(req.toEntity());
        createQuestion(survey, req.getQuestions());
    }

    private void createQuestion(Survey survey, List<QuestionDto.Create> questionDtos) {
        Integer tempId = 0;
        List<Question> questions = new ArrayList<>();

        Map<Integer, List<OptionDto.Create>> optionMap = new HashMap<>();
        for (QuestionDto.Create qDto : questionDtos) {
            Question question = qDto.toEntity(survey, tempId);
            questions.add(question);
            optionMap.put(tempId, qDto.getOptions());
            tempId++;
        }
        questionRepository.saveAll(questions);

        List<Option> options = new ArrayList<>();
        for (Question q : questions) {
            for (OptionDto.Create oDto : optionMap.get(q.getTempId())) {
                Option option = oDto.toEntity(q);
                options.add(option);
            }
        }
        optionRepository.saveAll(options);
    }

    public SurveyDto.Read getSurvey(Long id) {
        Survey survey = surveyRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Survey not found"));

        List<Question> questions = questionRepository.findAllBySurveyIdAndSurveyVersion(survey.getId(), survey.getVersion());
        List<Long> questionIds = questions.stream().map(Question::getId).toList();
        List<Option> options = optionRepository.findAllByQuestionIdIn(questionIds);

        List<QuestionDto.Read> questionDtos = new ArrayList<>();
        for (Question question : questions) {
            List<OptionDto.Read> optionDtos = new ArrayList<>();
            for (Option option : options) {
                if (option.getQuestionId().equals(question.getId())) {
                    optionDtos.add(OptionDto.Read.fromEntity(option));
                }
            }
            questionDtos.add(QuestionDto.Read.of(question, optionDtos));
        }

        return SurveyDto.Read.of(survey, questionDtos);
    }

    public void updateSurvey(Long id, SurveyDto.Update req) {
        Survey survey = surveyRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Survey not found"));

    }
}
