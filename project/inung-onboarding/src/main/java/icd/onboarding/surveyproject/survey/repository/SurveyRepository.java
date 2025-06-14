package icd.onboarding.surveyproject.survey.repository;

import icd.onboarding.surveyproject.survey.service.domain.Survey;

import java.util.UUID;

public interface SurveyRepository {
	Survey save (Survey survey);

	Survey findById (UUID surveyId);
}
