package com.INNER_CIRCLE_ICD4.innerCircle.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Answer {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(optional = false)
    private Response response;

    @ManyToOne(optional = false)
    private Question question;

    private String text;

    @ElementCollection
    private List<UUID> selectedOptions;

    public Answer(Response response, Question question, String text, List<UUID> selectedOptions) {
        this.response = response;
        this.question = question;
        this.text = text;
        this.selectedOptions = selectedOptions;
    }

    // ✅ 직접 추가: AnswerDto 에서 사용하기 위함
    public UUID getQuestionId() {
        return question.getId();
    }
}
