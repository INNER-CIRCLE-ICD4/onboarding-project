package kr.co.fastcampus.onboarding.hyeongminonboarding.domain.entity;


import jakarta.persistence.*;
import kr.co.fastcampus.onboarding.hyeongminonboarding.domain.entity.enums.QuestionType;
import kr.co.fastcampus.onboarding.hyeongminonboarding.global.entity.base.BaseTimeEntity;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SoftDelete;
import org.hibernate.annotations.Where;

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
@SQLDelete(sql = "UPDATE question SET deleted = true WHERE id = ?")
@Where(clause = "deleted = false")
public class Question extends BaseTimeEntity {
    @Id
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "survey_id", nullable = false)
    private Survey survey;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String detail;

    @Enumerated(EnumType.STRING)
    private QuestionType type;

    private boolean required;

    @Column(name = "deleted", nullable = false)
    @Builder.Default
    private boolean deleted = false;

}
