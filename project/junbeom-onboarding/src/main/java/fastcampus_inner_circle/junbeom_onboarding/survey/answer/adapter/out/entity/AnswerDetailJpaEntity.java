package fastcampus_inner_circle.junbeom_onboarding.survey.answer.adapter.out.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @Column(nullable = false)
    private Long contentId;

    @Column(length = 255)
    private String contentName;

    @Column(length = 255)
    private String contentDescribe;

    @Column(length = 255)
    private String type;

    @Column(name = "answer_value", length = 255)
    private String value;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "answer_id")
    private AnswerJpaEntity answer;

    @OneToMany(mappedBy = "answerDetail", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AnswerDetailOptionJpaEntity> options;

    public void setOptions(List<AnswerDetailOptionJpaEntity> options) {
        this.options = options;
    }
} 