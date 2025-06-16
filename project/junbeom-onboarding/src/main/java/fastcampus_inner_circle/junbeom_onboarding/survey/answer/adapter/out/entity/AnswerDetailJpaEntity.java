package fastcampus_inner_circle.junbeom_onboarding.survey.answer.adapter.out.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Entity
@Table(name = "answer_detail")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnswerDetailJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "answer_id")
    private AnswerJpaEntity answer;

    @Column(nullable = false)
    private Long contentId;

    private Long optionId;

    private String answerValue;
} 