package com.multi.sungwoongonboarding.submission.infrastructure.answers;

import com.multi.sungwoongonboarding.common.entity.BaseEntity;
import com.multi.sungwoongonboarding.options.domain.Options;
import com.multi.sungwoongonboarding.submission.domain.Answers;
import com.multi.sungwoongonboarding.submission.infrastructure.submission.SubmissionJpaEntity;
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
    @JoinColumn(name = "submission_id")
    private SubmissionJpaEntity submissionJpaEntity;

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

    public void mappingSubmissionJpaEntity(SubmissionJpaEntity submissionJpaEntity) {
        this.submissionJpaEntity = submissionJpaEntity;
        if (!submissionJpaEntity.getAnswers().contains(this)) {
            submissionJpaEntity.getAnswers().add(this);
        }
    }

    public void setOptions(Options options) {
        this.optionId = options.getId();
        this.answerText = options.getOptionText();
    }

    public Answers toDomain() {
        return Answers.builder()
                .id(this.id)
                .submissionId(this.submissionJpaEntity != null ? this.submissionJpaEntity.getId() : null)
                .questionId(this.questionId)
                .answerText(this.answerText)
                .build();
    }
}