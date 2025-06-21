package com.icd.onboarding.survey.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ListIndexJavaType;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
@AllArgsConstructor
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long surveyId;

    private Integer surveyVersion;

    @OneToMany(mappedBy = "answer")
    private List<AnswerDetail> answerDetails = new ArrayList<>();
}
