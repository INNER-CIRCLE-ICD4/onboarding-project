package com.icd.onboarding.survey.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
@AllArgsConstructor
public class AnswerDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "answer_id")
    private Long answerId;

    private Long questionId;

    private String textAnswer;

    private Long selectedOptionId;

    @ManyToOne
    @JoinColumn(name = "answer_id", insertable = false, updatable = false)
    private Answer answer;
}
