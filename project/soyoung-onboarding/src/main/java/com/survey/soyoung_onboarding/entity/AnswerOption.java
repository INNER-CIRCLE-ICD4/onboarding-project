package com.survey.soyoung_onboarding.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class AnswerOption {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "answer_id")
    private Answer answer;

    @ManyToMany
    @JoinTable(
            name = "question_option_mapping",
            joinColumns = @JoinColumn(name = "answer_option_id"),
            inverseJoinColumns = @JoinColumn(name = "question_option_id")
    )
    private List<QuestionOption> question_options = new ArrayList<>();

}