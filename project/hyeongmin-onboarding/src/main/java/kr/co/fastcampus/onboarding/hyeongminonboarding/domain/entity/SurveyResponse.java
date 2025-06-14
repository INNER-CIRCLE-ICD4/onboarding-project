package kr.co.fastcampus.onboarding.hyeongminonboarding.domain.entity;


import jakarta.persistence.*;
import kr.co.fastcampus.onboarding.hyeongminonboarding.global.entity.base.BaseTimeEntity;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


/***
 * @author khm0813
 * - 설문 조사 - 응답 맵핑 테이블
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "survey_response")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SurveyResponse extends BaseTimeEntity {
    @Id
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "survey_id", nullable = false)
    private Survey survey;

    private int surveyVersion;

    private LocalDateTime submittedAt;
}
