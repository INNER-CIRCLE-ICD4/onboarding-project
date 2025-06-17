package com.survey.soyoung_onboarding.service;

import com.survey.soyoung_onboarding.dto.QuestionDto;
import com.survey.soyoung_onboarding.dto.SurveyDto;
import com.survey.soyoung_onboarding.entity.Question;
import com.survey.soyoung_onboarding.entity.Survey;
import com.survey.soyoung_onboarding.repository.SurveyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SurveyService {

    @Autowired
    private SurveyRepository surveyRepository;

    public void createSurvey(SurveyDto dto) {
        Survey survey = createSurveyEntity(dto);
        survey.setQuestions(createQuestionEntities(dto.getQuestions(), survey));
        surveyRepository.save(survey);
    }

    private Survey createSurveyEntity(SurveyDto dto) {
        Survey survey = new Survey();
        survey.setTitle(dto.getTitle());
        survey.setDescription(dto.getDescription());
        survey.setReg_date(new Date());
        survey.setUpdate_date(new Date());
        return survey;
    }

    private List<Question> createQuestionEntities(List<QuestionDto> questionDtos, Survey survey) {
        return questionDtos.stream().map(dto -> {
            Question q = new Question();
            q.setTitle(dto.getTitle());
            q.setType(dto.getType());
            q.setRequired(dto.isRequired());
            q.setOptions(dto.getOptions());
            q.setStatus("0"); // 원본
            q.setSurvey(survey); // 연관관계 설정
            return q;
        }).collect(Collectors.toList());
    }

}
