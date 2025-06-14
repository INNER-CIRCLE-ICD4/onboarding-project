package com.INNER_CIRCLE_ICD4.innerCircle.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Choice {

    @Id
    @GeneratedValue
    private UUID id;

    private String text;            // 선택지 텍스트
    private int choiceIndex;        // 순서 정보 (선택적)

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private Question question;

    // ✅ 선택지 텍스트 + 순서 정보를 이용한 생성자
    public Choice(String text, int choiceIndex) {
        this.text = text;
        this.choiceIndex = choiceIndex;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // ✅ 선택지 텍스트 + 질문을 이용한 생성자 (테스트 코드에서 사용됨)
    public Choice(String text, Question question) {
        this.text = text;
        this.question = question;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void updateText(String newText) {
        this.text = newText;
        this.updatedAt = LocalDateTime.now();
    }

    public void setQuestion(Question question) {
        this.question = question;
    }
}
