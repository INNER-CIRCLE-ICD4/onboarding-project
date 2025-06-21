package com.example.byeongjin_onboarding.service;

import com.example.byeongjin_onboarding.dto.FormItemAnswerSummaryDto;
import com.example.byeongjin_onboarding.dto.SubmitAnswerRequest;
import com.example.byeongjin_onboarding.dto.SurveyAnswerSummaryResponse;
import com.example.byeongjin_onboarding.entity.AnswerItem;
import com.example.byeongjin_onboarding.entity.FormItem;
import com.example.byeongjin_onboarding.entity.ItemType;
import com.example.byeongjin_onboarding.entity.Survey;
import com.example.byeongjin_onboarding.entity.SurveyAnswer;
import com.example.byeongjin_onboarding.repository.AnswerItemRepository;
import com.example.byeongjin_onboarding.repository.FormItemRepository;
import com.example.byeongjin_onboarding.repository.SurveyAnswerRepository;
import com.example.byeongjin_onboarding.repository.SurveyRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

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
        Survey survey = surveyRepository.findById(request.getSurveyId())
                .orElseThrow(() -> new NoSuchElementException("응답하려는 설문조사를 찾을 수 없습니다. ID: " + request.getSurveyId()));

        SurveyAnswer surveyAnswer = new SurveyAnswer();
        surveyAnswer.setSurvey(survey);
        surveyAnswer.setRespondentIdentifier("Anonymous-" + System.currentTimeMillis());

        Map<Long, Object> responses = request.getResponses();

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

        for (Map.Entry<Long, Object> entry : responses.entrySet()) {
            Long formItemId = entry.getKey();
            Object answerContentObject = entry.getValue();

            FormItem formItem = formItemRepository.findById(formItemId)
                    .orElseThrow(() -> new NoSuchElementException("존재하지 않는 설문 항목에 대한 응답입니다. ID: " + formItemId));

            String answerContent;
            if (formItem.getItemType() == ItemType.MULTI_CHOICE && answerContentObject instanceof List) {
                answerContent = ((List<String>) answerContentObject).stream()
                        .map(String::trim)
                        .filter(s -> !s.isEmpty())
                        .collect(Collectors.joining(","));
            } else if (answerContentObject != null) {
                answerContent = answerContentObject.toString().trim();
            } else {
                answerContent = "";
            }

            if (!formItem.isRequired() && answerContent.isEmpty()) {
                continue;
            }

            AnswerItem answerItem = new AnswerItem(formItem, answerContent);
            surveyAnswer.addAnswerItem(answerItem);
        }

        surveyAnswerRepository.save(surveyAnswer);

        return "설문 응답이 성공적으로 제출되었습니다. 응답 ID: " + surveyAnswer.getId();
    }

    @Transactional
    public SurveyAnswerSummaryResponse getSurveyAnswerSummary(Long surveyId) {
        Survey survey = surveyRepository.findById(surveyId)
                .orElseThrow(() -> new NoSuchElementException("해당 ID의 설문조사를 찾을 수 없습니다: " + surveyId));

        List<SurveyAnswer> allAnswers = surveyAnswerRepository.findAllBySurveyIdWithAnswerItems(surveyId);

        long totalSubmissions = allAnswers.size();

        Map<Long, FormItemAnswerSummaryDto> summaryMap = new HashMap<>();

        survey.getFormItems().forEach(formItem -> {
            FormItemAnswerSummaryDto itemSummary = new FormItemAnswerSummaryDto();
            itemSummary.setFormItemId(formItem.getId());
            itemSummary.setFormItemName(formItem.getName());
            itemSummary.setFormItemDescription(formItem.getDescription());
            itemSummary.setItemType(formItem.getItemType());
            itemSummary.setRequired(formItem.isRequired());
            itemSummary.setDisplayOrder(formItem.getDisplayOrder());
            itemSummary.setOptionCounts(new HashMap<>());
            itemSummary.setTextAnswers(new ArrayList<>());
            summaryMap.put(formItem.getId(), itemSummary);
        });

        for (SurveyAnswer surveyAnswer : allAnswers) {
            for (AnswerItem answerItem : surveyAnswer.getAnswerItems()) {
                Long formItemId = answerItem.getFormItem().getId();
                FormItemAnswerSummaryDto itemSummary = summaryMap.get(formItemId);

                if (itemSummary == null) {
                    System.err.println("경고: 응답은 있지만 설문 질문 목록에 없는 FormItem ID: " + formItemId + " (무시됨)");
                    continue;
                }

                if (itemSummary.getItemType() == ItemType.SHORT_ANSWER || itemSummary.getItemType() == ItemType.LONG_ANSWER) {
                    String textAnswer = answerItem.getTextAnswer();
                    if (textAnswer != null && !textAnswer.trim().isEmpty()) {
                        itemSummary.getTextAnswers().add(textAnswer.trim());
                    }
                } else if (itemSummary.getItemType() == ItemType.SINGLE_CHOICE || itemSummary.getItemType() == ItemType.MULTI_CHOICE) {
                    for (String selectedOption : answerItem.getSelectedOptions()) {
                        itemSummary.getOptionCounts().merge(selectedOption, 1L, Long::sum);
                    }
                }
            }
        }

        List<FormItemAnswerSummaryDto> sortedSummaries = summaryMap.values().stream()
                .sorted((s1, s2) -> Integer.compare(s1.getDisplayOrder(), s2.getDisplayOrder()))
                .collect(Collectors.toList());

        return new SurveyAnswerSummaryResponse(
                survey.getId(),
                survey.getName(),
                survey.getDescription(),
                totalSubmissions,
                sortedSummaries
        );
    }
}