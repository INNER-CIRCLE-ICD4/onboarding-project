package icd.onboarding.surveyproject.survey.repository.entity;

import icd.onboarding.surveyproject.survey.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "answer")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AnswerEntity extends BaseEntity {
	@Id
	@EmbeddedId
	private AnswerId answerId;

	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("responseId")
	@JoinColumn(name = "response_id")
	private ResponseEntity response;

	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("questionId")
	@JoinColumn(name = "question_id")
	private QuestionEntity question;

	@Column(name = "text_value", nullable = false)
	private String textValue;
}