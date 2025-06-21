package com.example.byeongjin_onboarding.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class AnswerItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "survey_answer_id", nullable = false)
    private SurveyAnswer surveyAnswer; // 어떤 설문 응답에 속하는지

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "form_item_id", nullable = false)
    private FormItem formItem; // 어떤 설문 항목(질문)에 대한 응답인지

    @Column(columnDefinition = "TEXT")
    private String answerContent; // 응답 내용 (단답, 장문, 객관식 선택 값 등)

    public AnswerItem(FormItem formItem, String answerContent) {
        this.formItem = formItem;
        this.answerContent = answerContent;
    }
}