package icd.onboarding.surveyproject.survey.repository;

import icd.onboarding.surveyproject.survey.service.domain.Response;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ResponseRepository {
	Response save (Response response);

	Optional<List<Response>> findBySurveyIdAndSurveyVersion (UUID surveyId, int surveyVersion);

	boolean existsBySurveyIdAndVersionAndRespondentId (UUID surveyId, int surveyVersion, String respondentId);
}
