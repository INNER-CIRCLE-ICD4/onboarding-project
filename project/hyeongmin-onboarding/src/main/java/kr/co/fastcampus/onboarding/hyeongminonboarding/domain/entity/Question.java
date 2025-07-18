package kr.co.fastcampus.onboarding.hyeongminonboarding.domain.entity;


import jakarta.persistence.*;
import kr.co.fastcampus.onboarding.hyeongminonboarding.domain.entity.enums.QuestionType;
import kr.co.fastcampus.onboarding.hyeongminonboarding.global.entity.base.BaseTimeEntity;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


/**
 * @author khm0813
 * - 질문 테이블
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "question")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Question extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "survey_id", nullable = false)
    private Survey survey;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String detail;

    @Enumerated(EnumType.STRING)
    private QuestionType type;

    private boolean required; // 필수 여부

    /**
     * 하나의 질문에는 여러개 질문 옵션이 있을 수 있다. (Optional)
     * QuestionType
     *  -  SINGLE_CHOICE
     *  -  MULTIPLE_CHOICE
     */
    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QuestionOption> options;
}
