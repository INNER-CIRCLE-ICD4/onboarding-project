package com.multi.sungwoongonboarding.forms.infrastructure;

import com.multi.sungwoongonboarding.common.entity.BaseEntity;
import com.multi.sungwoongonboarding.forms.domain.Forms;
import com.multi.sungwoongonboarding.questions.infrastructure.QuestionJpaEntity;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
@Entity
@Table(name = "forms")
public class FormsJpaEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "form_id")
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "user_id")
    private String userId;

    @OneToMany(mappedBy = "formsJpaEntity", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<QuestionJpaEntity> questions = new ArrayList<>();

    public static FormsJpaEntity fromDomain(Forms form) {

        FormsJpaEntity formsJpaEntity = new FormsJpaEntity();
        formsJpaEntity.id = form.getId();
        formsJpaEntity.title = form.getTitle();
        formsJpaEntity.description = form.getDescription();

        if (form.getQuestions() != null && !form.getQuestions().isEmpty()) {
            form.getQuestions().forEach(question -> {

                QuestionJpaEntity questionJpaEntity = QuestionJpaEntity.fromDomain(question);
                questionJpaEntity.mappingFormJpaEntity(formsJpaEntity);
            });
        }

        return formsJpaEntity;
    }

    public FormsJpaEntity update(Forms form) {

        this.id = form.getId();
        this.title = form.getTitle();
        this.description = form.getDescription();

        if (form.getQuestions() != null && !form.getQuestions().isEmpty()) {
            form.getQuestions().forEach(question -> {
                Optional<QuestionJpaEntity> hasQuestion = this.questions.stream().filter(q -> q.getId().equals(question.getId())).findFirst();

                hasQuestion.ifPresentOrElse(questionJpaEntity -> questionJpaEntity.update(question), () -> {

                    QuestionJpaEntity questionJpaEntity = QuestionJpaEntity.fromDomain(question);
                    questionJpaEntity.mappingFormJpaEntity(this);

                });
            });
        }

        return this;
    }

    public void setQuestionList(List<QuestionJpaEntity> questions) {
        this.questions = questions;
    }

    public Forms toDomain() {
        return Forms.builder()
                .id(this.id)
                .title(this.title)
                .description(this.description)
                .questions(this.questions.stream().map(QuestionJpaEntity::toDomain).toList())
                .createdAt(this.getCreatedAt())
                .createdAt(this.getUpdatedAt())
                .userId(this.userId)
                .build();
    }

}
