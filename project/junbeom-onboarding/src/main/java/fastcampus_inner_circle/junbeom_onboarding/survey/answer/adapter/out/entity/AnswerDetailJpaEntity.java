package fastcampus_inner_circle.junbeom_onboarding.survey.answer.adapter.out.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.util.List;

@Entity
@Table(name = "survey_answer_detail")
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

    @Column(length = 255)
    private String answerValue;

    @Column(length = 255)
    private String questionContent;

    @OneToMany(mappedBy = "answerDetail", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AnswerDetailOptionJpaEntity> options;

    public void setOptions(List<AnswerDetailOptionJpaEntity> options) {
        this.options = options;
    }
} 