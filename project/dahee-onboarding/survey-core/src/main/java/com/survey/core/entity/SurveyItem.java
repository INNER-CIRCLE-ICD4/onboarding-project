package com.survey.core.entity;

import com.survey.core.enums.QuestionType;
import jakarta.persistence.*;
import lombok.*;

import java.util.*;

/**
 * 설문 문항 엔티티
 * 각 설문(Survey)에 여러 개의 문항(질문) 포함, FK(Long)로만 단방향 연결
 */
@Entity
@Table(name = "survey_item")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SurveyItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long surveyId;    // 설문 FK (Survey.id)

    @Column(nullable = false)
    private String question;  // 질문 텍스트

    private String description; // 질문 설명 (선택)

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private QuestionType type; // 문항 타입 (SHORT_TEXT, LONG_TEXT, SINGLE_CHOICE, MULTI_CHOICE)

    @Column(nullable = false)
    private boolean required;  // 필수 여부

    @ElementCollection //별도의 엔티티 없이 String 저장
    @CollectionTable( // 저장용 테이블 이름과 FK 컬럼을 지정
            name = "survey_item_option",
            joinColumns = @JoinColumn(name = "item_id")
    )
    @Column(name = "option_value")
    private List<String> options = new ArrayList<>(); // 선택지(객관식만)

    @Column(nullable = false)
    private boolean isDeleted = false; // 소프트 삭제 (표시/미표시 제어)


}

