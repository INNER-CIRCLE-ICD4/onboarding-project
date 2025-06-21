package icd.onboarding.surveyproject.survey.service;

import icd.onboarding.surveyproject.survey.repository.ResponseRepository;
import icd.onboarding.surveyproject.survey.repository.SurveyRepository;
import icd.onboarding.surveyproject.survey.service.domain.Response;
import icd.onboarding.surveyproject.survey.service.domain.Survey;
import icd.onboarding.surveyproject.survey.service.exception.DuplicateResponseException;
import icd.onboarding.surveyproject.survey.service.exception.SurveyNotFoundException;
import icd.onboarding.surveyproject.survey.service.exception.SurveyResponseNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class SurveyService {
	private final SurveyRepository surveyRepository;
	private final ResponseRepository responseRepository;

	SurveyService (SurveyRepository surveyRepository, ResponseRepository responseRepository) {
		this.surveyRepository = surveyRepository;
		this.responseRepository = responseRepository;
	}

	public Survey findByIdAndVersion (UUID id, int version) {
		return surveyRepository.findByIdAndVersion(id, version).orElseThrow(SurveyNotFoundException::new);
	}

	public Survey createSurvey (Survey survey) {
		return surveyRepository.save(survey);
	}

	public Survey updateSurvey (Survey existingSurvey) {
		surveyRepository.findByIdAndVersion(existingSurvey.getId(), existingSurvey.getVersion())
						.orElseThrow(SurveyNotFoundException::new);

		Survey updatedSurvey = existingSurvey.update(
				existingSurvey.getTitle(),
				existingSurvey.getDescription(),
				existingSurvey.getQuestions()
		);

		return surveyRepository.save(updatedSurvey);
	}

	public Response submitResponse (Response response) {
		boolean exists = responseRepository.existsBySurveyIdAndVersionAndRespondentId(
				response.getSurveyId(),
				response.getSurveyVersion(),
				response.getRespondentId()
		);

		if (exists) {
			throw new DuplicateResponseException();
		}

		Survey survey = surveyRepository.findByIdAndVersion(
				response.getSurveyId(),
				response.getSurveyVersion()
		).orElseThrow(SurveyNotFoundException::new);

		response.validateRequiredAnswers(survey);
		response.validateSingleSelectAnswers(survey);

		return responseRepository.save(response);
	}

	public List<Response> findResponsesBySurvey (UUID surveyId, int version) {
		return responseRepository.findBySurveyIdAndSurveyVersion(surveyId, version)
								 .orElseThrow(SurveyResponseNotFoundException::new);
	}
}
