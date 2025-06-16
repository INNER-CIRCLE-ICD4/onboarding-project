package icd.onboarding.surveyproject.survey.fixtures;

import icd.onboarding.surveyproject.survey.service.domain.Option;
import icd.onboarding.surveyproject.survey.service.domain.Question;
import icd.onboarding.surveyproject.survey.service.enums.InputType;

import java.util.List;

public class QuestionFixtures {
	public static Question basicQuestion () {
		List<Option> options = List.of(Option.create("옵션 1", 1));
		return Question.create(
				"질문 1",
				"질문 설명",
				InputType.SHORT_TEXT,
				true,
				1,
				options
		);
	}
}
