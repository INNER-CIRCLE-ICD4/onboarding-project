package survey.surveyresponse.client;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

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
            log.error("[SurveyClient.read()] surveyId: {}", surveyId, e);
            return null;
        }
    }
}
