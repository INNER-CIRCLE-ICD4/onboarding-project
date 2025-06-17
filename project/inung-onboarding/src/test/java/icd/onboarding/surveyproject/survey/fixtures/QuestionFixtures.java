package icd.onboarding.surveyproject.survey.fixtures;

import icd.onboarding.surveyproject.survey.service.domain.Option;
import icd.onboarding.surveyproject.survey.service.domain.Question;
import icd.onboarding.surveyproject.survey.service.enums.InputType;

import java.util.Collections;
import java.util.List;

public class QuestionFixtures {
	public static Question shortTextQuestion () {
		return Question.create(
				"단답형 질문",
				"단답형 질문 설명",
				InputType.SHORT_TEXT,
				true,
				1,
				Collections.emptyList()
		);
	}

	public static Question singleSelectQuestion () {
		return Question.create(
				"단일 선택형 질문",
				"단일 선택형 질문 설명",
				InputType.SINGLE_SELECT,
				true,
				1,
				List.of(
						Option.create("옵션 1", 1),
						Option.create("옵션 2", 2)
				)
		);
	}

	public static Question multiSelectQuestion () {
		return Question.create(
				"다중 선택형 질문",
				"다중 선택형 질문 설명",
				InputType.MULTI_SELECT,
				false,
				1,
				List.of(
						Option.create("옵션 1", 1),
						Option.create("옵션 2", 2)
				)
		);
	}

}
