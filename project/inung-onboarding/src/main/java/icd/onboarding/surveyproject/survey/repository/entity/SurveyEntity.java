package icd.onboarding.surveyproject.survey.repository.entity;

import icd.onboarding.surveyproject.survey.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "survey")
public class SurveyEntity extends BaseEntity {
	@Id
	@EmbeddedId
	private SurveyId surveyId;

	@Column(nullable = false, length = 200)
	private String title;

	@Column(length = 1000)
	private String description;

	@OneToMany(mappedBy = "survey", cascade = CascadeType.ALL, orphanRemoval = true)
	@OrderBy("sortOrder asc")
	private List<QuestionEntity> questions = new ArrayList<>();
}
