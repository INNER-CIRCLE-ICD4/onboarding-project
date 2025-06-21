package fastcampus.inguk_onboarding.form.post.repository.entity.post;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import fastcampus.inguk_onboarding.common.repository.entity.TimeBaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="survey_versions")
@Getter
@Setter
public class SurveyVersionEntity extends TimeBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "survey_id", nullable = false)
    @JsonBackReference
    private SurveyEntity survey;

    @Column(nullable = false, length = 50)
    private String versionCode;

    @OneToMany(mappedBy = "surveyVersion", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<SurveyItemEntity> items = new ArrayList<>();

    protected SurveyVersionEntity() {}

    public SurveyVersionEntity(SurveyEntity survey, String versionCode) {
        this.survey = survey;
        this.versionCode = versionCode;
    }

    public void setSurvey(SurveyEntity survey) {
        this.survey = survey;
    }

    public void addItem(SurveyItemEntity item) {
        items.add(item);
        item.setSurveyVersion(this);
    }

    public void removeItem(SurveyItemEntity item) {
        items.remove(item);
        item.setSurveyVersion(null);
    }

}
