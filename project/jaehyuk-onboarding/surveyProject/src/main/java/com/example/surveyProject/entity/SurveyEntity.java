package com.example.surveyProject.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * SurveyEntity
 *
 * @author Jpark
 * @date 2025-06-16
 * @Calss Survey
 */

@Entity
@Table(name = "survey")
@Getter
@Setter
public class SurveyEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;

    @OneToMany(mappedBy = "survey", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SurveyItemEntity> items;


    private String status;


}
