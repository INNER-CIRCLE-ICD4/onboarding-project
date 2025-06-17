package com.survey.soyoung_onboarding.entity;

import com.survey.soyoung_onboarding.domain.QuestionType;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
public class Question {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "survey_id")
    private Survey survey;

    /**
     *  질문 제목
     */
    private String title;

    /**
     *  질문 타입
     */
    @Enumerated(EnumType.STRING)
    private QuestionType type;

    /** 
     *  필수 항목 여부
     */
    private boolean required;

    /**
     *  선택형 질문의 경우 옵션값
     */
    @ElementCollection
    private List<String> options; // 선택형 질문에 해당
    
    /**
     * 수정 / 삭제 여부
     * 0: 원본
     * 1: 삭제됨
     * 2: 수정됨
     */
    private String status;

}
