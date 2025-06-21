package com.innercircle.onboarding.answer.domain;

import com.innercircle.onboarding.common.jpa.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "answer")
public class Answer extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("PK")
    @Column(name = "seq", nullable = false)
    private Long seq;

    @Comment("질문 FK")
    @Column(name = "question_seq", nullable = false)
    private Long questionSeq;

    @Comment("질문 원본")
    @Column(name = "origin_question", nullable = false, length = 100)
    private String originQuestion;

    @Comment("답변 내용")
    @Column(name = "content", nullable = false, length = 100)
    private String content;

    @Builder
    public Answer(Long seq, Long questionSeq, String originQuestion, String content) {
        this.seq = seq;
        this.questionSeq = questionSeq;
        this.originQuestion = originQuestion;
        this.content = content;
    }

}
