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
import java.util.NoSuchElementException;
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

    public SurveyCreateResponse getSurveyById(Long id) {
        Survey survey = surveyRepository.findById(id).orElseThrow(() -> new NoSuchElementException("해당 ID의 설문조사를 찾을 수 없습니다."));
        return convertToDto(survey);
    }

    @Transactional
    public SurveyCreateResponse updateSurvey(Long id, SurveyCreateRequest request) {
        Survey survey = surveyRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("해당 ID의 설문조사를 찾을 수 없습니다: " + id));

        survey.setName(request.getName());
        survey.setDescription(request.getDescription());

        if (request.getFormItems() != null) {
            List<FormItem> updatedFormItems = new ArrayList<>();
            for (FormItemDto dto : request.getFormItems()) {
                FormItem formItem = new FormItem(
                        dto.getName(),
                        dto.getDescription(),
                        ItemType.valueOf(dto.getItemType()),
                        dto.isRequired(),
                        dto.getDisplayOrder(),
                        dto.getOptions()
                );
                formItem.setId(dto.getId());
                updatedFormItems.add(formItem);
            }
            survey.updateFormItems(updatedFormItems);
        } else {
            survey.getFormItems().clear();
        }

        Survey updatedSurvey = surveyRepository.save(survey);
        return convertToDto(updatedSurvey);
    }

    @Transactional
    public void deleteSurvey(Long id) {
        if (!surveyRepository.existsById(id)) {
            throw new IllegalArgumentException("삭제하려는 설문조사가 존재하지 않습니다: " + id);
        }
        surveyRepository.deleteById(id);
    }

    public List<SurveyCreateResponse> getAllSurveys() {
        List<Survey> surveys = surveyRepository.findAll();
        return surveys.stream().map(this::convertToDto).collect(Collectors.toList());
    }
}