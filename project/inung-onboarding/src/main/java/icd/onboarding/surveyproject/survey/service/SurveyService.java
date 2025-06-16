package icd.onboarding.surveyproject.survey.service;

import icd.onboarding.surveyproject.survey.repository.SurveyRepository;
import icd.onboarding.surveyproject.survey.service.domain.Survey;

public class SurveyService {
	private final SurveyRepository surveyRepository;

	SurveyService (SurveyRepository surveyRepository) {
		this.surveyRepository = surveyRepository;
	}

	public Survey createSurvey (Survey survey) {
		return surveyRepository.save(survey);
	}

	public Survey updateSurvey (Survey existingSurvey) {
		Survey updatedSurvey = existingSurvey.update(
				existingSurvey.getTitle(),
				existingSurvey.getDescription(),
				existingSurvey.getQuestions()
		);

		return surveyRepository.save(updatedSurvey);
	}
}
