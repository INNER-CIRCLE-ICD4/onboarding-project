package com.fastcampus.survey.questionary.domain.model;

import com.fastcampus.survey.questionary.adapter.in.dto.InsertFormRequest;
import com.fastcampus.survey.questionary.adapter.in.mapper.SurveyFormMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SurveyForm {
    private Long id;
    private String name;
    private String describe;
    private LocalDateTime createAt;
    private List<SurveyContent> contents;

}
