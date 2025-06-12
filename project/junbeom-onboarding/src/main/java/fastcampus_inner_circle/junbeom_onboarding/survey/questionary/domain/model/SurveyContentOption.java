package fastcampus_inner_circle.junbeom_onboarding.survey.questionary.domain.model;

import lombok.Builder;
import lombok.Getter;

import java.util.Objects;

@Getter
@Builder
public class SurveyContentOption {
    private Long id;
    private Long contentId;
    private String text;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SurveyContentOption that = (SurveyContentOption) o;
        return Objects.equals(text, that.text); // text만 비교
    }

    @Override
    public int hashCode() {
        return Objects.hash(text); // text만 포함
    }
}