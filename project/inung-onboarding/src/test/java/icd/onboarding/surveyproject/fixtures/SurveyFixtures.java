package icd.onboarding.surveyproject.fixtures;

import icd.onboarding.surveyproject.service.domain.Question;
import icd.onboarding.surveyproject.service.domain.Survey;

import java.util.List;

public class SurveyFixtures {
	public static Survey basicSurvey () {
		List<Question> questions = List.of(QuestionFixtures.basicQuestion());
		return Survey.create("설문 조사 1", "설문 조사 양식", questions);
	}
}
