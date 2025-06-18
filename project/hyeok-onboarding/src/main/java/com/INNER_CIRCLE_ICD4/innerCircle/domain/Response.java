package com.INNER_CIRCLE_ICD4.innerCircle.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Response {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(optional = false)
    private Survey survey;

    @OneToMany(
            mappedBy = "response",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Answer> answers = new ArrayList<>();

    @Lob
    @Column(nullable = false)
    private String surveySnapshot; // 응답 시점의 설문 스냅샷 (JSON)

    public Response(Survey survey, String surveySnapshot) {
        this.survey = survey;
        this.surveySnapshot = surveySnapshot;
    }

    public void addAnswer(Answer answer) {
        answers.add(answer);
        answer.setResponse(this);
    }
}
