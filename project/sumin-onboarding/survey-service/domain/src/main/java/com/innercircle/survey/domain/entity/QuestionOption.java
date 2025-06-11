package com.innercircle.survey.domain.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class QuestionOption {
    //선택 리스트 ID(PK)
    @Id @GeneratedValue
    private Long id;
    //선택 항목
    private String option;
    //질문과 1:N
    @ManyToOne
    @JoinColumn(name = "question_id")
    private Question question;

    //선택 리스트 생성자
    private String createdBy;

    //선택 리스트 생성시간
    private LocalDateTime createdAt;

    //선택 리스트 수정자
    private String updatedBy;

    //선택 리스트 수정시간
    private LocalDateTime updatedAt;


}
