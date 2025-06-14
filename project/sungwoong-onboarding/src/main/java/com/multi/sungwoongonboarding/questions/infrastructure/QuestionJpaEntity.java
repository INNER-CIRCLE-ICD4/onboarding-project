package com.multi.sungwoongonboarding.questions.infrastructure;

import com.multi.sungwoongonboarding.common.entity.BaseEntity;
import com.multi.sungwoongonboarding.forms.infrastructure.FormsJpaEntity;
import com.multi.sungwoongonboarding.options.infrastructure.OptionsJpaEntity;
import com.multi.sungwoongonboarding.questions.domain.Questions;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "questions")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuestionJpaEntity extends BaseEntity {

    @Id
    @Column(name = "question_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "question_text", nullable = false)
    private String questionText;

    @Column(name = "question_type", nullable = false)
    private String questionType;

    @Column(name = "is_required", nullable = false)
    private boolean isRequired;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "form_id")
    private FormsJpaEntity formsJpaEntity;

    @Column(name = "version", nullable = false)
    private int version = 1;

    @Column(name = "deleted", nullable = false)
    private boolean deleted = false;

    @OneToMany(mappedBy = "questionJpaEntity", cascade = CascadeType.ALL)
    private List<OptionsJpaEntity> options = new ArrayList<>();



    public static QuestionJpaEntity fromDomain(Questions question) {

        QuestionJpaEntity questionJpaEntity = new QuestionJpaEntity();
        questionJpaEntity.questionText = question.getQuestionText();
        questionJpaEntity.questionType = question.getQuestionType().name();
        questionJpaEntity.isRequired = question.isRequired();

        if (question.getId() != null) {
            questionJpaEntity.id = question.getId();
            questionJpaEntity.version++;
        }

        if (question.getOptions() != null && !question.getOptions().isEmpty()) {
            question.getOptions().forEach(options -> {
                OptionsJpaEntity optionJpaEntity = OptionsJpaEntity.fromDomain(options);
                optionJpaEntity.mappingQuestionJpaEntity(questionJpaEntity);
            });
        }
        return questionJpaEntity;
    }

    public Questions toDomain() {
        return Questions.builder()
                .id(this.id)
                .questionText(this.questionText)
                .questionType(Questions.QuestionType.valueOf(this.questionType))
                .version(this.version)
                .isRequired(this.isRequired)
                .options(this.options.stream().map(OptionsJpaEntity::toDomain).toList())
                .build();
    }

    public void mappingFormJpaEntity(FormsJpaEntity formsJpaEntity) {
        this.formsJpaEntity = formsJpaEntity;
        if (!formsJpaEntity.getQuestions().contains(this)) {
            formsJpaEntity.getQuestions().add(this);
        }
    }
}
