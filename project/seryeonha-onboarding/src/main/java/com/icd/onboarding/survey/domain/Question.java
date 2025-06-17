package com.icd.onboarding.survey.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
@AllArgsConstructor
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long surveyId;

    private int order;

    private String name;

    private String description;

    @Enumerated(EnumType.STRING)
    private QuestionType type;

    private boolean required;

    private boolean active;
}
