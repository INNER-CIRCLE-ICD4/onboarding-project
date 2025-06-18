package survey.surveyresponse.client;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContextException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import survey.common.exception.ApiException;
import survey.common.exception.ErrorType;

import java.util.Optional;

import static survey.common.exception.ErrorType.RESOURCE_VERSION_MISMATCH;

@Slf4j
@Component
@RequiredArgsConstructor
public class SurveyClient {
    private RestClient restClient;
    @Value("${endpoints.survey-service.url}")
    private String surveyServiceUrl;

    @PostConstruct
    public void initRestClient() {
        this.restClient = RestClient.create(surveyServiceUrl);
    }

    public Long fetchSurveyFormId(Long surveyId) {
        try {
            Long response = restClient.get()
                    .uri("/api/v1/survey/fetch/{surveyId}", surveyId)
                    .retrieve()
                    .body(Long.class);
            return response;
        } catch (Exception e) {
            log.error("[ArticleClient.read()] articleId: {}", surveyId, e);
            return null;
        }
    }
}
