package com.INNER_CIRCLE_ICD4.innerCircle.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private java.util.UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "response_id")
    private Response response;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private Question question;

    private String text;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "answer_selected_options",
            joinColumns = @JoinColumn(name = "answer_id"),
            inverseJoinColumns = @JoinColumn(name = "choice_id")
    )
    private Set<Choice> selectedOptions = new HashSet<>();

    public Answer(Response response, Question question, String text, java.util.List<java.util.UUID> selectedOptionIds) {
        this.response = response;
        this.question = question;
        this.text = text;
        if (selectedOptionIds != null) {
            for (java.util.UUID id : selectedOptionIds) {
                Choice choice = new Choice();
                choice.setId(id);
                this.selectedOptions.add(choice);
            }
        }
    }

    public void addSelectedOption(Choice choice) {
        this.selectedOptions.add(choice);
    }
}
