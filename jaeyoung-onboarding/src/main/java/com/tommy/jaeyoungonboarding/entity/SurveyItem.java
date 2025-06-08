package com.tommy.jaeyoungonboarding.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "survey_item")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SurveyItem {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // 항목이름
    @Column(name = "item_title")
    private String surveyItemTitle;

    // 항목설명
    @Column(name = "item_description")
    private String surveyItemDescription;

    // 항목 입력 형태
    @Column(name = "item_survey_form")
    @Enumerated(EnumType.STRING)
    private SurveyItemForm surveyItemForm;

    // 항목 필수 여부
    @Column(name = "item_essential")
    private boolean itemEssential;

    // surveyId
    @Column(name = "survey_id")
    private UUID surveyId;
}
