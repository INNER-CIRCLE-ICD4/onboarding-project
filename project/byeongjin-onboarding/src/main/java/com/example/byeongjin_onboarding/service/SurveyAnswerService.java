package com.example.byeongjin_onboarding.service;

import com.example.byeongjin_onboarding.entity.*;
import com.example.byeongjin_onboarding.dto.SubmitAnswerRequest;
import com.example.byeongjin_onboarding.repository.AnswerItemRepository;
import com.example.byeongjin_onboarding.repository.FormItemRepository;
import com.example.byeongjin_onboarding.repository.SurveyAnswerRepository;
import com.example.byeongjin_onboarding.repository.SurveyRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@Service
public class SurveyAnswerService {

    @Autowired
    private SurveyRepository surveyRepository;
    @Autowired
    private FormItemRepository formItemRepository;
    @Autowired
    private SurveyAnswerRepository surveyAnswerRepository;
    @Autowired
    private AnswerItemRepository answerItemRepository;

    @Transactional
    public String submitAnswer(SubmitAnswerRequest request) {
        Survey survey = surveyRepository.findById(request.getSurveyId()).orElseThrow(() -> new NoSuchElementException("응답하려는 설문조사를 찾을 수 없습니다. ID: " + request.getSurveyId()));

        SurveyAnswer surveyAnswer = new SurveyAnswer();
        surveyAnswer.setSurvey(survey);

        Map<Long, Object> responses = request.getResponses();
        for (Map.Entry<Long, Object> entry : responses.entrySet()) {
            Long formItemId = entry.getKey();
            Object answerContentObject = entry.getValue();

            FormItem formItem = formItemRepository.findById(formItemId).orElseThrow(() -> new NoSuchElementException("존재하지 않는 설문 항목에 대한 응답입니다. ID: " + formItemId));

            String answerContent;
            if (formItem.getItemType() == ItemType.MULTI_CHOICE && answerContentObject instanceof List) {
                answerContent = String.join(",", (List<String>) answerContentObject);
            } else if (answerContentObject != null) {
                answerContent = answerContentObject.toString();
            } else {
                answerContent = "";
            }

            AnswerItem answerItem = new AnswerItem(formItem, answerContent);
            surveyAnswer.addAnswerItem(answerItem);
        }

        for (FormItem formItem : survey.getFormItems()) {
            if (formItem.isRequired()) {
                if (!responses.containsKey(formItem.getId()) ||
                        responses.get(formItem.getId()) == null ||
                        (responses.get(formItem.getId()) instanceof String && ((String)responses.get(formItem.getId())).trim().isEmpty()) ||
                        (responses.get(formItem.getId()) instanceof List && ((List<?>)responses.get(formItem.getId())).isEmpty())) {
                    throw new IllegalArgumentException("필수 항목 '" + formItem.getName() + "'에 대한 응답이 누락되었습니다.");
                }
            }
        }

        surveyAnswerRepository.save(surveyAnswer);

        return "설문 응답이 성공적으로 제출되었습니다. 응답 ID: " + surveyAnswer.getId();
    }
}