package com.multi.sungwoongonboarding.submission.infrastructure.answers;

import com.multi.sungwoongonboarding.common.entity.BaseEntity;
import com.multi.sungwoongonboarding.options.domain.Options;
import com.multi.sungwoongonboarding.submission.domain.Answers;
import com.multi.sungwoongonboarding.submission.infrastructure.responses.ResponseJpaEntity;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
@Table(name = "answers")
public class AnswerJpaEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "answer_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "response_id")
    private ResponseJpaEntity responseJpaEntity;

    @Column(name = "question_id", nullable = false)
    private Long questionId;

    @Column(name = "option_id")
    private Long optionId;

    @Column(name = "answer_text")
    private String answerText;


    public static AnswerJpaEntity fromDomain(Answers answers) {
        AnswerJpaEntity answerJpaEntity = new AnswerJpaEntity();
        answerJpaEntity.id = answers.getId();
        answerJpaEntity.questionId = answers.getQuestionId();
        answerJpaEntity.optionId = answers.getOptionId();
        answerJpaEntity.answerText = answers.getAnswerText();
        return answerJpaEntity;
    }

    public void setResponseJpaEntity(ResponseJpaEntity responseJpaEntity) {
        this.responseJpaEntity = responseJpaEntity;
        if (!responseJpaEntity.getAnswers().contains(this)) {
            responseJpaEntity.getAnswers().add(this);
        }
    }

    public void setOptions(Options options) {
        this.optionId = options.getId();
        this.answerText = options.getOptionText();
    }

    public Answers toDomain() {
        return Answers.builder()
                .id(this.id)
                .responseId(this.responseJpaEntity != null ? this.responseJpaEntity.getId() : null)
                .questionId(this.questionId)
                .answerText(this.answerText)
                .build();
    }
}