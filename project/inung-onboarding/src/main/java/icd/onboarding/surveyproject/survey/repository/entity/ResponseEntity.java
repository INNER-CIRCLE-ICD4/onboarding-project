package icd.onboarding.surveyproject.survey.repository.entity;

import icd.onboarding.surveyproject.survey.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Table(name = "response")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ResponseEntity extends BaseEntity {
	@Id
	@Column(name = "response_id", columnDefinition = "BINARY(16)")
	private UUID id;

	@Embedded
	private SurveyId surveyId;

	@Column(name = "respondentId", length = 200, nullable = false)
	private String respondentId;

	@OneToMany(mappedBy = "response", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<AnswerEntity> answers = new ArrayList<>();
}