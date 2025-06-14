package kr.co.fastcampus.onboarding.hyeongminonboarding.domain.entity;


import jakarta.persistence.*;
import kr.co.fastcampus.onboarding.hyeongminonboarding.global.entity.base.BaseTimeEntity;
import lombok.*;

import java.util.ArrayList;
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

    @Column(columnDefinition = "TEXT")
    private String answerText;

    @ElementCollection
    private List<Long> selectedOptionIds;

    @Column(columnDefinition = "TEXT")
    private String questionSnapshotJson;

    @Column(columnDefinition = "TEXT")
    private String optionSnapshotJson;


}
