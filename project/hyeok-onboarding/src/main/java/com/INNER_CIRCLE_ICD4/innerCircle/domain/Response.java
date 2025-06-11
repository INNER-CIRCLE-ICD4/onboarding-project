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
public class Response {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "survey_id")
    private Survey survey;

    @Lob
    private String surveySnapshot; // ✅ 누락된 부분 추가

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "response", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Answer> answers = new ArrayList<>();

    public Response(Survey survey, String surveySnapshot) {
        this.survey = survey;
        this.surveySnapshot = surveySnapshot;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public void addAnswer(Answer answer) {
        answer.setResponse(this);
        this.answers.add(answer);
    }
}
