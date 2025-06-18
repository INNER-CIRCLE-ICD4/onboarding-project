package com.example.surveyProject.service;

import com.example.surveyProject.common.SurveyInputType;
import com.example.surveyProject.common.repository.SurveyRepository;
import com.example.surveyProject.dto.SurveyDto;
import com.example.surveyProject.entity.SurveyEntity;
import com.example.surveyProject.entity.SurveyItemEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 설문 조사 수정Service
 *
 * @author Jpark
 * @date 2025-06-18
 * @Calss
 */

@Service
@RequiredArgsConstructor
public class SurveyUpdateService {

    private final SurveyRepository surveyRepository;

    public Long updateSurvey(SurveyDto request) {

        Long listCount = request.getItems().stream()
                .filter(item -> "0".equals(item.getStatus()))
                .count();


        if (listCount == null || listCount < 1 || listCount > 10) {
            throw new IllegalArgumentException("10개 이상 넣으면 앙대요.");
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
            entity.setItemName(item.getItemName());
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
