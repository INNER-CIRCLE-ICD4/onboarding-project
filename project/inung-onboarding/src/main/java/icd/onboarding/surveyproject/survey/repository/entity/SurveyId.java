package icd.onboarding.surveyproject.survey.repository.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Embeddable
public class SurveyId implements Serializable {
	@Column(name = "survey_id", columnDefinition = "BINARY(16)")
	private UUID id;

	@Column(name = "version")
	private int version;

	@Override
	public boolean equals (Object other) {
		if (this == other) return true;
		if (!(other instanceof SurveyId that)) return false;
		return version == that.version && Objects.equals(id, that.id);
	}

	@Override
	public int hashCode () {
		return Objects.hash(id, version);
	}
}
