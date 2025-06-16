package com.multi.sungwoongonboarding.responses.infrastructure.answers;

import com.multi.sungwoongonboarding.common.entity.BaseEntity;
import com.multi.sungwoongonboarding.options.domain.Options;
import com.multi.sungwoongonboarding.questions.domain.Questions;
import com.multi.sungwoongonboarding.responses.domain.Answers;
import com.multi.sungwoongonboarding.responses.infrastructure.responses.ResponseJpaEntity;
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

    // 질문 스냅샷 데이터
    @Column(name = "original_question_text", nullable = false)
    private String originalQuestionText;

    @Column(name = "original_question_version", nullable = false)
    private int originalQuestionVersion;

    @Column(name = "original_question_is_required", nullable = false)
    private boolean originalQuestionIsRequired;

    @Column(name = "original_question_type", nullable = false)
    private Questions.QuestionType originalQuestionType;


    public static AnswerJpaEntity fromDomain(Answers answers) {
        AnswerJpaEntity answerJpaEntity = new AnswerJpaEntity();
        answerJpaEntity.id = answers.getId();
        answerJpaEntity.questionId = answers.getQuestionId();
        answerJpaEntity.optionId = answers.getOptionId();
        answerJpaEntity.answerText = answers.getAnswerText();
        answerJpaEntity.originalQuestionText = answers.getOriginalQuestionText();
        answerJpaEntity.originalQuestionVersion = answers.getOriginalQuestionVersion();
        answerJpaEntity.originalQuestionIsRequired = answers.isOriginalQuestionIsRequired();
        answerJpaEntity.originalQuestionType = answers.getOriginalQuestionType();
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
                .originalQuestionText(this.originalQuestionText)
                .originalQuestionVersion(this.originalQuestionVersion)
                .originalQuestionIsRequired(this.originalQuestionIsRequired)
                .originalQuestionType(this.originalQuestionType)
                .build();
    }
}