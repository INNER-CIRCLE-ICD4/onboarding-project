package icd.onboarding.surveyproject.survey.repository.entity;

import icd.onboarding.surveyproject.survey.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Getter
@Table(name = "options")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OptionEntity extends BaseEntity {
	@Id
	@Column(name = "option_id", columnDefinition = "BINARY(16)")
	private UUID id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "question_id", nullable = false)
	private QuestionEntity question;

	@Column(nullable = false, length = 1000)
	private String text;

	@Column(name = "sort_order", nullable = false)
	private int sortOrder;
}