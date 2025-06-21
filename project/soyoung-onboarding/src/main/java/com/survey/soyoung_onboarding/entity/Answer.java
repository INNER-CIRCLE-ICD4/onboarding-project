package com.survey.soyoung_onboarding.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Answer {

    @Id
    @GeneratedValue
    private Long id;

    private String name;
    private String email;

    @ManyToOne
    @JoinColumn(name = "reply_id")
    private Reply reply;

    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;

    /**
     * 사용자가 선택한 답변
     * 단답형/장문형: 텍스트 저장
     */
    private String answerText;

    /**
     * 선택형(단일/다중): 옵션 저장
     */
    @OneToMany(mappedBy = "answer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AnswerOption> selected_options = new ArrayList<>(); // 단일/다중 선택형일 경우만 사용
}
