package com.icd.onboarding.survey.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
@AllArgsConstructor
public class Option {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "question_id")
    private Long questionId;

    @ManyToOne
    @JoinColumn(name = "question_id", insertable = false, updatable = false)
    private Question question;

    private Integer orderNum;

    private String content;

    private boolean active;
}
