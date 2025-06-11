package com.INNER_CIRCLE_ICD4.innerCircle.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
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

    private String textValue; // ✅ 여기 추가

    @OneToMany(mappedBy = "answer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AnswerChoice> selectedChoices = new ArrayList<>();

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Answer(Question question, String textValue) {
        this.question = question;
        this.textValue = textValue;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void addChoice(AnswerChoice answerChoice) {
        answerChoice.setAnswer(this);
        this.selectedChoices.add(answerChoice);
    }

    public void setResponse(Response response) {
        this.response = response;
    }
}
