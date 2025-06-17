package fc.icd.jaehyeononboarding.survey.model.entity;

import fc.icd.jaehyeononboarding.common.converter.StringArrayJsonConverter;
import fc.icd.jaehyeononboarding.common.model.InputType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString(exclude = {"questionGroup"})
@Entity
@Table(name = "question")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_group_id")
    private QuestionGroup questionGroup;

    @Column(nullable = false)
    private Integer position;
    @Column(nullable = false)
    private String label;
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private InputType inputType;
    @Column(nullable = false)
    private Boolean required;

    @Convert(converter = StringArrayJsonConverter.class)
    private List<String> options;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    public static Question create(Integer position,  String label, String description, InputType inputType
            , Boolean required, List<String> options) {
        Question question = new Question();
        question.setPosition(position);
        question.setLabel(label);
        question.setDescription(description);
        question.setInputType(inputType);
        question.setRequired(required);
        question.setOptions(options);
        question.setCreatedAt(LocalDateTime.now());
        return question;
    }

}
