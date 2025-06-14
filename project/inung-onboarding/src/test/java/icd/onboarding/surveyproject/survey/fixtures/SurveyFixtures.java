package icd.onboarding.surveyproject.survey.fixtures;

import icd.onboarding.surveyproject.survey.service.domain.Option;
import icd.onboarding.surveyproject.survey.service.domain.Question;
import icd.onboarding.surveyproject.survey.service.domain.Survey;

import java.util.Collections;
import java.util.List;

public class SurveyFixtures {
	public static Survey basicSurvey () {
		return Survey.create(
				"설문 1",
				"설문 조사 설명",
				List.of(
						Question.create("텍스트 질문", "질문 1 설명", "SHORT_TEXT", false, 1, Collections.emptyList()),
						Question.create("선택형 질문", "질문 2 설명", "SINGLE_SELECT", true, 1, List.of(Option.create("옵션 1", 1)))
				)
		);
	}
}
