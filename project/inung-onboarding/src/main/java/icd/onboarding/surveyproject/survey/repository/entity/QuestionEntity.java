package icd.onboarding.surveyproject.survey.repository.entity;

import icd.onboarding.surveyproject.survey.common.entity.BaseEntity;
import icd.onboarding.surveyproject.survey.service.enums.InputType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Table(name = "questions")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuestionEntity extends BaseEntity {
	@Id
	@Column(name = "question_id", columnDefinition = "BINARY(16)")
	private UUID id;

	@ManyToOne
	@JoinColumns({
			@JoinColumn(name = "survey_id", referencedColumnName = "survey_id"),
			@JoinColumn(name = "version", referencedColumnName = "version")
	})
	private SurveyEntity survey;

	@Column(length = 200, nullable = false)
	private String name;

	@Column(length = 1000)
	private String description;

	@Enumerated(EnumType.STRING)
	@Column(name = "input_type", nullable = false)
	private InputType inputType;

	@Column(nullable = false)
	private boolean required;

	@Column(name = "sort_order", nullable = false)
	private int sortOrder;

	@OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
	@OrderBy("sortOrder asc")
	private List<OptionEntity> options = new ArrayList<>();

}
