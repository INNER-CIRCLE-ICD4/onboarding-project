package com.example.byeongjin_onboarding.service;

import com.example.byeongjin_onboarding.entity.FormItem;
import com.example.byeongjin_onboarding.entity.ItemType;
import com.example.byeongjin_onboarding.entity.Survey;
import com.example.byeongjin_onboarding.dto.FormItemDto;
import com.example.byeongjin_onboarding.dto.SurveyCreateRequest;
import com.example.byeongjin_onboarding.dto.SurveyCreateResponse;
import com.example.byeongjin_onboarding.repository.SurveyRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SurveyService {

    @Autowired
    private SurveyRepository surveyRepository;

    @Transactional
    public SurveyCreateResponse createSurvey(SurveyCreateRequest request) {
        Survey survey = new Survey();
        survey.setName(request.getName());
        survey.setDescription(request.getDescription());

        if (request.getFormItems() != null) {
            List<FormItem> formItems = request.getFormItems().stream()
                    .map(dto -> new FormItem(
                            dto.getName(),
                            dto.getDescription(),
                            ItemType.valueOf(dto.getItemType()),
                            dto.isRequired(),
                            dto.getDisplayOrder(),
                            dto.getOptions()
                    ))
                    .collect(Collectors.toList());
            survey.updateFormItems(formItems);
        }

        Survey savedSurvey = surveyRepository.save(survey);
        return convertToDto(savedSurvey);
    }

    private SurveyCreateResponse convertToDto(Survey survey) {
        List<FormItemDto> formItemDtos = survey.getFormItems().stream()
                .map(formItem -> new FormItemDto(
                        formItem.getId(),
                        formItem.getName(),
                        formItem.getDescription(),
                        formItem.getItemType().toString(),
                        formItem.isRequired(),
                        formItem.getDisplayOrder(),
                        new ArrayList<>(formItem.getOptions())
                ))
                .collect(Collectors.toList());

        return new SurveyCreateResponse(
                survey.getId(),
                survey.getName(),
                survey.getDescription(),
                survey.getCreatedAt(),
                survey.getUpdatedAt(),
                formItemDtos
        );
    }
}