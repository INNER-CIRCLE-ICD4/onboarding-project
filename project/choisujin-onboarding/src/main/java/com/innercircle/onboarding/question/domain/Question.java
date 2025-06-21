package com.innercircle.onboarding.question.domain;

import com.innercircle.onboarding.common.jpa.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "question")
public class Question extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Comment("PK")
    @Column(name = "seq", nullable = false)
    private Long seq;

    @Comment("폼 FK")
    @Column(name = "form_seq", nullable = false)
    private Long formSeq;

    @Comment("질문 내용")
    @Column(name = "content", nullable = false, length = 100)
    private String content;

    @Comment("질문 설명")
    @Lob
    @Column(name = "description")
    private String description;

    @Comment("답변 타입")
    @Column(name = "answer_type", nullable = false, length = 20)
    private String answerType;

    @Comment("답변 선택지")
    @Lob
    @Column(name = "answer_option")
    private String answerOption;

    @Comment("필수 여부")
    @Column(name = "is_required", nullable = false)
    private boolean isRequired = false;

    @Comment("버전")
    @Column(name = "version", nullable = false)
    private int version;

    @Comment("삭제 여부")
    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false;

    @Comment("표시 순서")
    @Column(name = "order_idx", nullable = false)
    private int orderIdx;

    @Builder
    public Question(Long seq, Long formSeq, String content, String description, String answerType, String answerOption, boolean isRequired, int version, boolean isDeleted, int orderIdx) {
        this.seq = seq;
        this.formSeq = formSeq;
        this.content = content;
        this.description = description;
        this.answerType = answerType;
        this.answerOption = answerOption;
        this.isRequired = isRequired;
        this.version = version;
        this.isDeleted = isDeleted;
        this.orderIdx = orderIdx;
    }

}
