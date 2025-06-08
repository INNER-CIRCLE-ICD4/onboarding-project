package com.multi.sungwoongonboarding.questions.infrastructure;

import com.multi.sungwoongonboarding.forms.infrastructure.FormsJpaEntity;
import com.multi.sungwoongonboarding.questions.domain.Questions;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "questions")
public class QuestionJpaEntity {

    @Id
    @Column(name = "question_id")
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    @Column(name = "question_text", nullable = false)
    private String questionText;

    @Column(name = "question_type", nullable = false)
    private String questionType;

    @Column(name = "order", nullable = false)
    private int order;

    @Column(name = "is_required", nullable = false)
    private boolean isRequired;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "form_id")
    private FormsJpaEntity formsJpaEntity;

    public static QuestionJpaEntity fromDomain(Questions questions) {

        QuestionJpaEntity questionJpaEntity = new QuestionJpaEntity();
        questionJpaEntity.id = questions.getId();
        questionJpaEntity.questionText = questions.getQuestionText();
        questionJpaEntity.questionType = questions.getQuestionType().name();
        questionJpaEntity.order = questions.getOrder();
        questionJpaEntity.isRequired = questions.isRequired();
        return questionJpaEntity;
    }

    public void mappingFormJpaEntity(FormsJpaEntity formsJpaEntity) {
        this.formsJpaEntity = formsJpaEntity;
    }
}
