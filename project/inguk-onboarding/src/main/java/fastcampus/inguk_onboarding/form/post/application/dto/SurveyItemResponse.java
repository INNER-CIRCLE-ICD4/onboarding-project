package fastcampus.inguk_onboarding.form.post.application.dto;

import fastcampus.inguk_onboarding.form.post.domain.Surveys.InputType;
import fastcampus.inguk_onboarding.form.post.repository.entity.post.SurveyItemEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class SurveyItemResponse {

    private Long id;
    private String title;
    private String description;
    private InputType inputType;
    private Boolean required;
    private Integer order;
    private List<String> options;
    public SurveyItemResponse() {}

    public SurveyItemResponse(SurveyItemEntity item) {
        this.id = item.getId();
        this.title = item.getTitle();
        this.description = item.getDescription();
        this.inputType = item.getInputType();
        this.required = item.getRequired();
        this.order = item.getOrder();
        this.options = item.getOptions();
    }


}