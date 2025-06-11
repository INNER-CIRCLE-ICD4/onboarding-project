package com.INNER_CIRCLE_ICD4.innerCircle.survey.request.domain;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Survey {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    private String description;

    @OneToMany(mappedBy = "survey", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Question> questions = new ArrayList<>();

    @OneToMany(mappedBy = "survey", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Response> responses = new ArrayList<>();

    // === 연관 관계 메서드 ===
    public void addQuestion(Question question) {
        questions.add(question);
        question.setSurvey(this);
    }

    public void addResponse(Response response) {
        responses.add(response);
        response.setSurvey(this);
    }

    // === Getters/Setters ===
    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public List<Response> getResponses() {
        return responses;
    }
}