// QuestionUpdateRequest.java
package com.INNER_CIRCLE_ICD4.innerCircle.dto;

import com.INNER_CIRCLE_ICD4.innerCircle.domain.QuestionType;
import java.util.List;

public class QuestionUpdateRequest {
    private Long id;
    private String title;
    private String description;
    private QuestionType type;
    private boolean required;
    private List<String> choices;

    public QuestionUpdateRequest() {} // ✅ 기본 생성자 추가

    public QuestionUpdateRequest(Long id, String title, String description,
                                 QuestionType type, boolean required, List<String> choices) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.type = type;
        this.required = required;
        this.choices = choices;
    }

    // Getters & Setters
    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public QuestionType getType() { return type; }
    public boolean isRequired() { return required; }
    public List<String> getChoices() { return choices; }

    public void setId(Long id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setDescription(String description) { this.description = description; }
    public void setType(QuestionType type) { this.type = type; }
    public void setRequired(boolean required) { this.required = required; }
    public void setChoices(List<String> choices) { this.choices = choices; }
}
