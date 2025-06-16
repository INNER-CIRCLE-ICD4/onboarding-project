package com.innercircle.onboarding.answer;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "answer")
public class Answer {


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

    @Comment("생성일시")
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, columnDefinition = "DATETIME(3)")
    private LocalDateTime createdAt;

    @Comment("수정일시")
    @UpdateTimestamp
    @Column(name = "modified_at", nullable = false, columnDefinition = "DATETIME(3)")
    private LocalDateTime modifiedAt;

}
