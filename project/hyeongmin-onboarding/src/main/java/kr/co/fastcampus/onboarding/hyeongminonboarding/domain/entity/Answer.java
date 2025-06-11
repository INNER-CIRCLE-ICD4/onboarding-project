package kr.co.fastcampus.onboarding.hyeongminonboarding.domain.entity;


import jakarta.persistence.*;
import kr.co.fastcampus.onboarding.hyeongminonboarding.global.entity.base.BaseTimeEntity;
import lombok.*;

import java.util.List;

/**
 * @author khm0813
 * - 설문조사 응답 테이블
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "answer")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Answer extends BaseTimeEntity {
    @Id
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "response_id", nullable = false)
    private SurveyResponse response;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    // 단답/장문형 답변인 경우
    @Column(columnDefinition = "TEXT")
    private String answerText;

    // 선택형 답변인 경우
    @ManyToMany
    @JoinTable(
            name = "answer_selected_options",
            joinColumns = @JoinColumn(name = "answer_id"),
            inverseJoinColumns = @JoinColumn(name = "option_id")
    )
    private List<QuestionOption> selectedOptions;
}
