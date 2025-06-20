package fastcampus_inner_circle.junbeom_onboarding.survey.questionary.domain.model;

import java.util.List;

import fastcampus_inner_circle.junbeom_onboarding.survey.questionary.adapter.out.entity.SurveyContentType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SurveyContent {
    private Long id;
    private Long formId;
    private String name;
    private String describe;
    private SurveyContentType type;
    private boolean isRequired;
    private List<SurveyContentOption> options;

    public SurveyContent() {
    }

    public SurveyContent(Long id, Long formId, String name, String describe, SurveyContentType type, boolean isRequired, List<SurveyContentOption> options) {
        this.id = id;
        this.formId = formId;
        this.name = name;
        this.describe = describe;
        this.type = type;
        this.isRequired = isRequired;
        this.options = options;
    }

    public void update(String name, String describe, SurveyContentType type, boolean isRequired, java.util.List<SurveyContentOption> options) {
        this.name = name;
        this.describe = describe;
        this.type = type;
        this.isRequired = isRequired;
        this.options = options;
    }
}
