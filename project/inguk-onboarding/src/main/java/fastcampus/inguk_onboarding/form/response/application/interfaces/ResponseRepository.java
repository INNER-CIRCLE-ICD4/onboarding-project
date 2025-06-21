package fastcampus.inguk_onboarding.form.response.application.interfaces;

import fastcampus.inguk_onboarding.form.response.Response;

import java.util.List;

public interface ResponseRepository {

    Response save(Response response);
    Response findById(Long id);
    List<Response> findBySurveyId(Long surveyId);
}
