package fastcampus.inguk_onboarding.form.post.repository.entity.post;

import fastcampus.inguk_onboarding.common.repository.entity.TimeBaseEntity;
import jakarta.persistence.*;
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

    protected SurveyEntity() {}

    public SurveyEntity(String title, String description) {
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
}
