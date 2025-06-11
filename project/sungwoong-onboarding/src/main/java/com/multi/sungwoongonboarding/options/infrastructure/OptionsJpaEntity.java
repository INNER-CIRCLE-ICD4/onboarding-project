package com.multi.sungwoongonboarding.options.infrastructure;

import com.multi.sungwoongonboarding.options.domain.Options;
import com.multi.sungwoongonboarding.questions.infrastructure.QuestionJpaEntity;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "options")
public class OptionsJpaEntity {

    @Id
    @Column(name = "option_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "option_text", nullable = false)
    private String optionText;

    @Column(name = "option_order")
    private int order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private QuestionJpaEntity questionJpaEntity;


    public static OptionsJpaEntity fromDomain(Options options) {
        OptionsJpaEntity optionsJpaEntity = new OptionsJpaEntity();
        optionsJpaEntity.id = options.getId();
        optionsJpaEntity.optionText = options.getOptionText();
        optionsJpaEntity.order = options.getOrder();
        return optionsJpaEntity;
    }

    public Options toDomain() {
        return new Options(id, optionText, order);
    }

    public void mappingQuestionJpaEntity(QuestionJpaEntity questionJpaEntity) {
        this.questionJpaEntity = questionJpaEntity;
        if (!questionJpaEntity.getOptions().contains(this)) {
            questionJpaEntity.getOptions().add(this);
        }
    }

}
