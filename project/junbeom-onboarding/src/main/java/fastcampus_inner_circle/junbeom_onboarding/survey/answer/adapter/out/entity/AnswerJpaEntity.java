package fastcampus_inner_circle.junbeom_onboarding.survey.answer.adapter.out.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "survey_answer")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnswerJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long formId;

    @Column(name="form_name", length = 100)
    private String formName;

    @Column(nullable = false)
    private LocalDateTime submittedAt;

    @OneToMany(mappedBy = "answer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AnswerDetailJpaEntity> details;

    public void setDetails(List<AnswerDetailJpaEntity> details) {
        this.details = details;
    }
} 