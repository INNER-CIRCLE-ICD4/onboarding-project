package com.survey.soyoung_onboarding.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
public class Answer {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "survey_id")
    private Survey survey;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;

    private String user_name;
    private String email;

    /**
     * 사용자가 선택한 답변
     * 단답형/장문형: text 저장
     * 선택형: 선택지 value 저장 (여러개일 경우 JSON으로 처리 가능)
     */
    private String answerText;

    private Date answeredAt;
}
