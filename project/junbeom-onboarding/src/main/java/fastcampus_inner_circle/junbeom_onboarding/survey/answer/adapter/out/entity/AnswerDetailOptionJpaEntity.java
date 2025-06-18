package fastcampus_inner_circle.junbeom_onboarding.survey.answer.adapter.out.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Entity
@Table(name = "survey_answer_detail_option")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnswerDetailOptionJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "answer_detail_id")
    private AnswerDetailJpaEntity answerDetail;

    @Column(length = 100)
    private String text;
} 