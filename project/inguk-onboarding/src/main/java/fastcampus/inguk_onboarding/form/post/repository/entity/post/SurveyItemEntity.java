package fastcampus.inguk_onboarding.form.post.repository.entity.post;

import fastcampus.inguk_onboarding.common.repository.entity.TimeBaseEntity;
import fastcampus.inguk_onboarding.form.post.domain.Surveys.InputType;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name="survey_items")
@Getter
@Setter
public class SurveyItemEntity extends TimeBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "survey_version_id", nullable = false)
    private SurveyVersionEntity surveyVersion;

    @Column(nullable = false, length = 200)
    private String title;

    @Column(length = 500)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InputType inputType;

    @Column(nullable = false)
    private Boolean required = false;

    @Column(name = "item_order", nullable = false)
    private Integer order;

    @ElementCollection
    @CollectionTable(name = "survey_item_options", joinColumns = @JoinColumn(name = "survey_item_id"))
    @Column(name = "option_value")
    private List<String> options;



    protected SurveyItemEntity() {}

    public SurveyItemEntity(String title, String description, InputType inputType, Boolean required, Integer order) {
        this.title = title;
        this.description = description;
        this.inputType = inputType;
        this.required = required;
        this.order = order;
    }

    public void setSurveyVersion(SurveyVersionEntity surveyVersion) {
        this.surveyVersion = surveyVersion;
    }
}
