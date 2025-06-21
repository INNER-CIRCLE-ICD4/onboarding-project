package onboarding.survey.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import onboarding.survey.exception.BadRequestException;

@Entity
public class Choice {

    @Id
    @GeneratedValue
    private Long id;

    @Getter
    private String optionValue;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    private SurveyItem surveyItem;

    protected Choice() {
    }

    public Choice(String optionValue) {
        validateValue(optionValue);
        this.optionValue = optionValue;
    }

    private void validateValue(String value) {
        if (value == null || value.isBlank()) {
            throw new BadRequestException("선택지 값은 필수입니다.");
        }
    }

}