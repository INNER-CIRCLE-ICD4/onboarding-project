package icd.onboarding.surveyproject.survey.service.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Getter
@EqualsAndHashCode(of = {"id", "version"})
public class Survey {
	private final UUID id;
	private final String title;
	private final String description;
	private final int version;

	private final List<Question> questions = new ArrayList<>();

	private Survey (UUID id, int version, String title, String description, List<Question> inputQuestions) {
		this.title = title;
		this.description = description;
		this.id = id;
		this.version = version;

		for (Question question : inputQuestions) {
			questions.add(question);
			question.assignSurvey(this);
		}
	}

	public static Survey create (String title, String description, List<Question> questions) {
		return new Survey(UUID.randomUUID(), 1, title, description, questions);
	}

	public Survey update (String title, String description, List<Question> updatedQuestions) {
		return new Survey(
				this.id,
				this.version + 1,
				title,
				description,
				updatedQuestions
		);
	}

	public Set<UUID> extractQuestionIdsCondition (Predicate<Question> condition) {
		return questions.stream()
						.filter(condition)
						.map(Question::getId)
						.collect(Collectors.toSet());
	}
}
