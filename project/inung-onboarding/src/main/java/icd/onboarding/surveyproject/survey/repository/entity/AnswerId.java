package icd.onboarding.surveyproject.survey.repository.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Embeddable
public class AnswerId implements Serializable {
	@Column(name = "response_id", columnDefinition = "BINARY(16)")
	private UUID responseId;

	@Column(name = "question_id", columnDefinition = "BINARY(16)")
	private UUID questionId;

	@Override
	public boolean equals (Object other) {
		if (this == other) return true;
		if (!(other instanceof AnswerId that)) return false;
		return Objects.equals(responseId, that.responseId) && Objects.equals(questionId, that.questionId);
	}

	@Override
	public int hashCode () {
		return Objects.hash(responseId, questionId);
	}
}