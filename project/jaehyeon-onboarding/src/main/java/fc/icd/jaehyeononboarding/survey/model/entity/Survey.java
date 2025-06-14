package fc.icd.jaehyeononboarding.survey.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@Entity
@Table(name = "survey")
public class Survey {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private Boolean isDeleted;

    @Column(nullable = false)
    private Integer latestVersion;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "survey", cascade = CascadeType.ALL)
    @OrderBy("version DESC")
    private List<QuestionGroup> questionGroups;

    public static Survey create(String name, String description, Boolean isDeleted) {
        Survey survey = new Survey();
        survey.setName(name);
        survey.setDescription(description);
        survey.setIsDeleted(isDeleted);
        survey.setLatestVersion(1);
        survey.setCreatedAt(LocalDateTime.now());
        return survey;
    }

}
