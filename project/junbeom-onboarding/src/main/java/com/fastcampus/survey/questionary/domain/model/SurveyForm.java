package com.fastcampus.survey.questionary.domain.model;

import com.fastcampus.survey.questionary.adapter.in.dto.InsertFormRequest;
import lombok.Builder;
import lombok.Getter;
import java.time.LocalDateTime;
import java.util.List;

@Getter
public class SurveyForm {
    private Long id;
    private String name;
    private String describe;
    private LocalDateTime createAt;
    private List<SurveyContent> contents;

    public SurveyForm(InsertFormRequest insertFormRequest) {
        // TODO: 비즈니스 로직 처리
    }
} 