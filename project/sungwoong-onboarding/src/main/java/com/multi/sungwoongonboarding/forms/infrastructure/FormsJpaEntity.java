package com.multi.sungwoongonboarding.forms.infrastructure;

import com.multi.sungwoongonboarding.common.entity.BaseEntity;
import com.multi.sungwoongonboarding.forms.domain.Forms;
import com.multi.sungwoongonboarding.options.infrastructure.OptionsJpaEntity;
import com.multi.sungwoongonboarding.questions.infrastructure.QuestionJpaEntity;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

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

    @OneToMany(mappedBy = "formsJpaEntity", cascade = CascadeType.ALL, orphanRemoval = true)
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

                if (question.getOptions() != null && !question.getOptions().isEmpty()) {
                    question.getOptions().forEach(options -> {

                        OptionsJpaEntity optionJpaEntity = OptionsJpaEntity.fromDomain(options);
                        optionJpaEntity.mappingQuestionJpaEntity(questionJpaEntity);

                    });
                }
            });
        }


        return formsJpaEntity;
    }

    public Forms toDomain() {
        return Forms.builder()
                .id(this.id)
                .title(this.title)
                .description(this.description)
                .questions(questions.stream().map(QuestionJpaEntity::toDomain).toList())
                .build();
    }
}
