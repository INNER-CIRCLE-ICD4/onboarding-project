package kr.co.fastcampus.onboarding.hyeongminonboarding.domain.entity;


import jakarta.persistence.*;
import kr.co.fastcampus.onboarding.hyeongminonboarding.global.entity.base.BaseTimeEntity;
import lombok.*;


/**
 * @author khm0813
 * - 질문 문항 테이블 (Optional)
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "question_option")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class QuestionOption extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @Column(name = "option_value", columnDefinition = "VARCHAR(512)")
    private String optionValue;
}
