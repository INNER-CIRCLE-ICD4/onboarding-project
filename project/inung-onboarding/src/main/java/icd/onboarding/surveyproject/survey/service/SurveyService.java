package icd.onboarding.surveyproject.survey.service;

import icd.onboarding.surveyproject.survey.repository.ResponseRepository;
import icd.onboarding.surveyproject.survey.repository.SurveyRepository;
import icd.onboarding.surveyproject.survey.service.domain.Question;
import icd.onboarding.surveyproject.survey.service.domain.Response;
import icd.onboarding.surveyproject.survey.service.domain.Survey;
import icd.onboarding.surveyproject.survey.service.exception.RequiredQuestionNotAnsweredException;
import icd.onboarding.surveyproject.survey.service.exception.SurveyNotFoundException;
import icd.onboarding.surveyproject.survey.service.exception.TooManyAnswersException;

import java.util.Set;
import java.util.UUID;

public class SurveyService {
	private final SurveyRepository surveyRepository;
	private final ResponseRepository responseRepository;

	SurveyService (SurveyRepository surveyRepository, ResponseRepository responseRepository) {
		this.surveyRepository = surveyRepository;
		this.responseRepository = responseRepository;
	}

	public Survey createSurvey (Survey survey) {
		return surveyRepository.save(survey);
	}

	public Survey updateSurvey (Survey existingSurvey) {
		Survey updatedSurvey = existingSurvey.update(existingSurvey.getTitle(), existingSurvey.getDescription(), existingSurvey.getQuestions());

		return surveyRepository.save(updatedSurvey);
	}

	public Response submitResponse (Response response) {
		Survey survey = surveyRepository.findByIdAndVersion(
				response.getSurveyId(),
				response.getSurveyVersion()
		).orElseThrow(SurveyNotFoundException::new);

		response.validateRequiredAnswers(survey);
		response.validateSingleSelectAnswers(survey);

		for (Question question : survey.getQuestions()) {
			question.validateRequiredAnswer(response.getAnswers());
		}

		return responseRepository.save(response);
	}
}
