package com.fastcampus.survey.questionary.adapter.in.mapper;

import com.fastcampus.survey.questionary.adapter.in.dto.InsertContentRequest;
import com.fastcampus.survey.questionary.adapter.in.dto.InsertFormRequest;
import com.fastcampus.survey.questionary.adapter.out.entity.SurveyContentType;
import com.fastcampus.survey.questionary.domain.model.SurveyContent;
import com.fastcampus.survey.questionary.domain.model.SurveyContentOption;
import com.fastcampus.survey.questionary.domain.model.SurveyForm;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class SurveyFormMapper {

    static public SurveyForm toSurveyForm(InsertFormRequest request) {
        return SurveyForm.builder()
                .name(request.getName())
                .describe(request.getDescribe())
                .createAt(LocalDateTime.now())
                .contents(convertContents(request.getContents()))
                .build();
    }

    private static List<SurveyContent> convertContents(List<InsertContentRequest> contentRequests) {
        if (contentRequests == null) {
            return null;
        }

        return contentRequests.stream()
                .map(SurveyFormMapper::convertContent)
                .collect(Collectors.toList());
    }

    private static SurveyContent convertContent(InsertContentRequest request) {
        return SurveyContent.builder()
                .name(request.getName())
                .describe(request.getDescribe())
                .type(convertContentType(request.getType()))
                .isRequired(request.isRequired())
                .options(convertContentOptions(request.getOptions()))
                .build();
    }

    private static SurveyContentType convertContentType(String type) {
        if (type == null) {
            return null;
        }

        try {
            return SurveyContentType.valueOf(type.toUpperCase());
        } catch (IllegalArgumentException e) {
            // Default to SHORT_ANSWER if the type is not recognized
            return SurveyContentType.SHORT_ANSWER;
        }
    }

    private static List<SurveyContentOption> convertContentOptions(List<String> options) {
        if (options == null) {
            return null;
        }

        return options.stream()
                .map(option -> SurveyContentOption.builder()
                        .text(option)
                        .build())
                .collect(Collectors.toList());
    }



} 