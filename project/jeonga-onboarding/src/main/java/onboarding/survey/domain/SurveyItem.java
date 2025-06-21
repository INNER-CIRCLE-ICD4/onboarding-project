package onboarding.survey.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import onboarding.survey.exception.BadRequestException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
public class SurveyItem {

    @Id
    @Getter
    @GeneratedValue
    private Long id;

    @Getter
    private String name;

    private String description;

    @Enumerated(EnumType.STRING)
    private InputType inputType;

    @Getter
    private boolean required;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    private Survey survey;

    @OneToMany(mappedBy = "surveyItem", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Choice> choices = new ArrayList<>();

    protected SurveyItem() {}

    public SurveyItem(String name, String description, InputType inputType, boolean required, List<Choice> choices) {
        validateName(name);
        validateChoices(inputType, choices);
        this.name = name;
        this.description = description;
        this.inputType = inputType;
        this.required = required;
        if (choices != null) {
            choices.forEach(this::addChoice);
        }
    }

    public void update(String name, String description, InputType type, boolean required, List<Choice> newChoices) {
        validateName(name);
        validateChoices(type, newChoices);

        this.name = name;
        this.description = description;
        this.inputType = type;
        this.required = required;
        this.choices.clear();

        if (newChoices != null) {
            newChoices.forEach(this::addChoice);
        }
    }

    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new BadRequestException("항목 이름은 필수입니다.");
        }
    }

    private void validateChoices(InputType inputType, List<Choice> choices) {
        if (inputType.isSelectable() && (choices == null || choices.isEmpty())) {
            throw new BadRequestException("선택형은 선택지가 필수입니다.");
        }
    }

    public void addChoice(Choice choice) {
        choice.setSurveyItem(this);
        this.choices.add(choice);
    }

    public List<Choice> getChoices() {
        return Collections.unmodifiableList(choices);
    }

}