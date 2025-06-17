package fastcampus.inguk_onboarding.form.post.repository.entity.post;

import fastcampus.inguk_onboarding.common.repository.entity.TimeBaseEntity;
import fastcampus.inguk_onboarding.form.post.domain.Survey;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="serveys")
@Getter
@AllArgsConstructor
public class SurveyEntity extends TimeBaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String title;

    @Column(length = 1000)
    private String description;

    @OneToMany(mappedBy = "survey", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<SurveyItemEntity> items = new ArrayList<>();


    public SurveyEntity(Survey survey) {
        this.description = survey.getDescription();
        this.id = survey.getId();
        this.items = (List<SurveyItemEntity>) survey.getItem();
        this.title = survey.getTitle();
    }

    public SurveyEntity( String title, String description) {
       this.title = title;
       this.description = description;
    }


    public void addItem(SurveyItemEntity item) {
        items.add(item);
        item.setSurvey(this);
    }

    public void removeItem(SurveyItemEntity item) {
        items.remove(item);
        item.setSurvey(null);
    }

    public Survey toSurvey() {
        return Survey.builder()
                .id(id)
                .title(title)
                .description(description)
                .item((SurveyItemEntity) items)
                .build();
    }
}
