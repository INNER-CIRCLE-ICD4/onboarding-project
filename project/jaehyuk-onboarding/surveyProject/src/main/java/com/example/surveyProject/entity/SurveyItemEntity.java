package com.example.surveyProject.entity;

import com.example.surveyProject.common.SurveyInputType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * SurveyEntity
 *
 * @author Jpark
 * @date 2025-06-16
 * @Calss Survey
 */

@Entity
@Table(name = "survey_item")
@Getter
@Setter
public class SurveyItemEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;

    @Enumerated(EnumType.STRING)
    private SurveyInputType inputType;

    private boolean required;

    @ElementCollection
    private List<String> options;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "survey_id")
    private SurveyEntity survey;

}
