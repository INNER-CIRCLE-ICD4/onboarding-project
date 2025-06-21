package com.icd.onboarding.survey.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
@AllArgsConstructor
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "survey_id")
    private Long surveyId;

    private Integer surveyVersion;

    @ManyToOne
    @JoinColumn(name = "survey_id", insertable = false, updatable = false)
    private Survey survey;

    private Integer orderNum;

    private String name;

    private String description;

    @Enumerated(EnumType.STRING)
    private QuestionType type;

    private boolean required;

    private boolean active;

    @OneToMany(mappedBy = "question")
    private List<Option> options = new ArrayList<>();

    @Transient
    private Integer tempId;
}
