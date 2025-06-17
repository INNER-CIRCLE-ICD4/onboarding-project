package icd.onboarding.surveyproject.survey.service.domain;

import icd.onboarding.surveyproject.survey.service.enums.InputType;
import icd.onboarding.surveyproject.survey.service.exception.RequiredQuestionNotAnsweredException;
import icd.onboarding.surveyproject.survey.service.exception.TooManyAnswersException;
import lombok.Getter;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Getter
public class Response {
	private final UUID responseId;
	private final UUID respondentId;
	private final UUID surveyId;
	private final Integer surveyVersion;
	private final List<Answer> answers;

	private Response (UUID surveyId, Integer surveyVersion, List<Answer> answers) {
		this.responseId = UUID.randomUUID();
		this.respondentId = UUID.randomUUID();
		this.surveyId = surveyId;
		this.surveyVersion = surveyVersion;

		for (Answer answer : answers) {
			answer.assignResponse(this);
		}

		this.answers = List.copyOf(answers);
	}

	public static Response create (UUID surveyId, Integer surveyVersion, List<Answer> answers) {
		return new Response(surveyId, surveyVersion, answers);
	}

	public Set<UUID> getAnsweredQuestionIds () {
		return answers.stream().map(Answer::getQuestionId).collect(Collectors.toSet());
	}

	public void validateRequiredAnswers (Survey survey) {
		Set<UUID> requiredQuestionIds = survey.extractQuestionIdsCondition(Question::isRequired);
		Set<UUID> answeredQuestionIds = getAnsweredQuestionIds();

		if (!answeredQuestionIds.containsAll(requiredQuestionIds)) {
			throw new RequiredQuestionNotAnsweredException();
		}
	}

	public void validateSingleSelectAnswers (Survey survey) {
		Set<UUID> singleSelectQuestionIds = survey.extractQuestionIdsCondition(p -> p.getInputType() == InputType.SINGLE_SELECT);
		Map<UUID, Long> countPerQuestion = answers.stream().filter(a -> singleSelectQuestionIds.contains(a.getQuestionId())).collect(Collectors.groupingBy(Answer::getQuestionId, Collectors.counting()));

		boolean hasManyAnswered = countPerQuestion.values().stream().anyMatch(cnt -> cnt > 1);

		if (hasManyAnswered) {
			throw new TooManyAnswersException();
		}
	}
}
