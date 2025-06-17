package com.example.surveyProject.service;

import com.example.surveyProject.common.SurveyInputType;
import com.example.surveyProject.common.repository.SurveyRepository;
import com.example.surveyProject.dto.SurveyCreateRequestDto;
import com.example.surveyProject.dto.SurveyItemCreateRequestDto;
import com.example.surveyProject.entity.SurveyEntity;
import com.example.surveyProject.entity.SurveyItemEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 설문조사 질문 생성
 *
 * @author Jpark
 * @date 2025-06-16
 * @Calss SurveyService
 */
@Service
@RequiredArgsConstructor
public class SurveyService {

    private final SurveyRepository surveyRepository;

    public Long createSurvey(SurveyCreateRequestDto request) {
        if (request.getItems() == null || request.getItems().size() < 1 || request.getItems().size() > 10) {
            throw new IllegalArgumentException("항목은 1개 이상 10개 이하로 입력해야 합니다.");
        }

        SurveyEntity survey = new SurveyEntity();
        survey.setTitle(request.getTitle());
        survey.setDescription(request.getDescription());

        List<SurveyItemEntity> itemEntities = request.getItems().stream().map(item -> {
            if ((item.getInputType() == SurveyInputType.SINGLE_SELECT || item.getInputType() == SurveyInputType.MULTI_SELECT)
                    && (item.getOptions() == null || item.getOptions().isEmpty())) {
                throw new IllegalArgumentException("선택형 질문은 옵션이 반드시 포함되어야 합니다.");
            }

            SurveyItemEntity entity = new SurveyItemEntity();
            entity.setName(item.getName());
            entity.setDescription(item.getDescription());
            entity.setInputType(item.getInputType());
            entity.setRequired(item.isRequired());
            entity.setOptions(item.getOptions());
            entity.setSurvey(survey);
            return entity;
        }).toList();

        survey.setItems(itemEntities);
        return surveyRepository.save(survey).getId();
    }
}

