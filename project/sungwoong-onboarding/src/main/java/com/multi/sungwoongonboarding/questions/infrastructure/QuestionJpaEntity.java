package com.multi.sungwoongonboarding.questions.infrastructure;

import com.multi.sungwoongonboarding.common.entity.BaseEntity;
import com.multi.sungwoongonboarding.forms.infrastructure.FormsJpaEntity;
import com.multi.sungwoongonboarding.options.infrastructure.OptionsJpaEntity;
import com.multi.sungwoongonboarding.questions.domain.Questions;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "questions")
public class QuestionJpaEntity extends BaseEntity {

    @Id
    @Column(name = "question_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "question_text", nullable = false)
    private String questionText;

    @Column(name = "question_type", nullable = false)
    private String questionType;

    @Column(name = "question_order", nullable = false)
    private int order;

    @Column(name = "is_required", nullable = false)
    private boolean isRequired;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "form_id")
    private FormsJpaEntity formsJpaEntity;

    @OneToMany(mappedBy = "questionJpaEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OptionsJpaEntity> options = new ArrayList<>();


    public static QuestionJpaEntity fromDomain(Questions questions) {

        QuestionJpaEntity questionJpaEntity = new QuestionJpaEntity();
        questionJpaEntity.id = questions.getId();
        questionJpaEntity.questionText = questions.getQuestionText();
        questionJpaEntity.questionType = questions.getQuestionType().name();
        questionJpaEntity.order = questions.getOrder();
        questionJpaEntity.isRequired = questions.isRequired();
        return questionJpaEntity;
    }

    public static Questions toDomain(QuestionJpaEntity questionJpaEntity) {
        return Questions.builder()
                .id(questionJpaEntity.getId())
                .questionText(questionJpaEntity.getQuestionText())
                .questionType(Questions.QuestionType.valueOf(questionJpaEntity.getQuestionType()))
                .order(questionJpaEntity.getOrder())
                .isRequired(questionJpaEntity.isRequired())
                .build();
    }

    public void mappingFormJpaEntity(FormsJpaEntity formsJpaEntity) {
        this.formsJpaEntity = formsJpaEntity;
        if (!formsJpaEntity.getQuestions().contains(this)) {
            formsJpaEntity.getQuestions().add(this);
        }
    }
}
