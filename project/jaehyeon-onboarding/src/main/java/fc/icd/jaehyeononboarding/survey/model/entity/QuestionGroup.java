package fc.icd.jaehyeononboarding.survey.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString(exclude = {"survey"})
@Entity
@Table(name = "question_group")
public class QuestionGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "survey_id")
    private Survey survey;

    @Column(nullable = false)
    private Integer version;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "questionGroup", cascade = CascadeType.ALL)
    @OrderBy("position ASC")
    private List<Question> questions;

    public static QuestionGroup create(Integer version) {
        QuestionGroup questionGroup = new QuestionGroup();
        questionGroup.setVersion(version);
        questionGroup.setCreatedAt(LocalDateTime.now());
        return questionGroup;
    }

}
