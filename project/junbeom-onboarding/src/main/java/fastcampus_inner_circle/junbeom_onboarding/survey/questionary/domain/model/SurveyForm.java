package fastcampus_inner_circle.junbeom_onboarding.survey.questionary.domain.model;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SurveyForm {
    private Long id;
    private String name;
    private String describe;
    private LocalDateTime createAt;
    private List<SurveyContent> contents;
}
