package com.survey.core.entity;

import com.survey.core.enums.QuestionType;
import jakarta.persistence.*;
import lombok.*;

import java.util.*;

/**
 * 개별 질문 모델링 및 동적 문항 옵션 관리
 * 1~10 문항 제어
 *
 */
@Entity
@Table(name = "survey_item")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SurveyItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "survey_id", nullable = false)
    private Survey survey;

    /**질문 텍스트*/
    @Column(nullable = false)
    private String question;

    /**질문 타입*/
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private QuestionType type;

    /**필수 여부*/
    @Column(nullable = false)
    private boolean required;

    /**선택지*/
    @ElementCollection //별도의 엔티티 없이 String 저장
    @CollectionTable( // 저장용 테이블 이름과 FK 컬럼을 지정
            name = "survey_item_option",
            joinColumns = @JoinColumn(name = "item_id")
    )
    @Column(name = "option_value")
    private List<String> options = new ArrayList<>();

    /**문항 보여주는 여부*/
    @Column(nullable = false)
    private boolean isDeleted = false;

    //양방향 연관관계를 위한 SETsURVEY
    public void setSurvey(Survey copy) {
    }
}
