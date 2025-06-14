package com.INNER_CIRCLE_ICD4.innerCircle.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Answer {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "response_id")
    private Response response;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private Question question;

    private String textValue; // 텍스트형 응답

    @OneToMany(mappedBy = "answer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AnswerChoice> choices = new ArrayList<>();

    // ✅ 생성자 추가 (ResponseService에서 사용)
    public Answer(Question question, String textValue) {
        this.question = question;
        this.textValue = textValue;
    }

    public void setResponse(Response response) {
        this.response = response;
    }

    // ✅ 선택지 추가 메서드
    public void addChoice(AnswerChoice answerChoice) {
        answerChoice.setAnswer(this);
        this.choices.add(answerChoice);
    }
}
