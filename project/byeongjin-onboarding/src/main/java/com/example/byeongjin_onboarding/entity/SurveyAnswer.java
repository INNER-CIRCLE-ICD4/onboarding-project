package com.example.byeongjin_onboarding.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "survey_answers")
@Getter
@Setter
@NoArgsConstructor
public class SurveyAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "survey_id", nullable = false)
    private Survey survey;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime submissionDate;

    @Column(name = "respondent_identifier")
    private String respondentIdentifier;

    @OneToMany(mappedBy = "surveyAnswer", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<AnswerItem> answerItems = new ArrayList<>();

    public void addAnswerItem(AnswerItem answerItem) {
        answerItems.add(answerItem);
        answerItem.setSurveyAnswer(this);
    }

    public void removeAnswerItem(AnswerItem answerItem) {
        answerItems.remove(answerItem);
        answerItem.setSurveyAnswer(null);
    }
}