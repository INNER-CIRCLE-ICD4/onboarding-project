package icd.onboarding.surveyproject.survey.fixtures;

import icd.onboarding.surveyproject.survey.service.domain.Option;
import icd.onboarding.surveyproject.survey.service.domain.Question;
import icd.onboarding.surveyproject.survey.service.domain.Survey;
import icd.onboarding.surveyproject.survey.service.enums.InputType;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class SurveyFixtures {
	public static Survey basicSurvey () {
		return Survey.create(
				"설문 1",
				"설문 조사 설명",
				List.of(
						Question.create("텍스트 질문", "질문 1 설명", InputType.SHORT_TEXT, false, 1, Collections.emptyList()),
						Question.create("선택형 질문", "질문 2 설명", InputType.SINGLE_SELECT, true, 1, List.of(Option.create("옵션 1", 1)))
				)
		);
	}

	public static Survey emptyQuestionSurvey () {
		return Survey.create(
				"질문이 없는 설문 조사",
				"질문이 없는 설문 조사 설명",
				null
		);
	}

	public static Survey updatedSurvey (UUID id, int version) {
		Survey survey = Survey.create("수정된 제목", "수정된 설명", List.of(
				Question.create("텍스트 질문", "질문 1 설명", InputType.SHORT_TEXT, false, 1, Collections.emptyList()),
				Question.create("선택형 질문", "질문 2 설명", InputType.SINGLE_SELECT, true, 1, List.of(Option.create("옵션 1", 1)))
		));
		ReflectionTestUtils.setField(survey, "id", id);
		ReflectionTestUtils.setField(survey, "version", version + 1);
		return survey;
	}
}
