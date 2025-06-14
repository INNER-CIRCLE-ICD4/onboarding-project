package icd.onboarding.surveyproject.fixtures;

import icd.onboarding.surveyproject.service.domain.Option;
import icd.onboarding.surveyproject.service.domain.Question;

import java.util.List;

public class QuestionFixtures {
	public static Question basicQuestion () {
		List<Option> options = List.of(Option.create("옵션 1", 1));
		return Question.create(
				"질문 1",
				"질문 설명",
				"SHORT_TEXT",
				true,
				1,
				options
		);
	}
}
