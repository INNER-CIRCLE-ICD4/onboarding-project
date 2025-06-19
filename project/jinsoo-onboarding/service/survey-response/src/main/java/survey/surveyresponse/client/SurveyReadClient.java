package survey.surveyresponse.client;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import survey.surveyresponse.service.response.surveyresponsequery.SurveyResponse;

@Slf4j
@Component
@RequiredArgsConstructor
public class SurveyReadClient {
    private RestClient restClient;

    @Value("${endpoints.survey-read-service.url}")
    private String surveyReadServiceUrl;

    @PostConstruct
    public void initRestClient() {
        this.restClient = RestClient.create(surveyReadServiceUrl);
    }

    public void saveResponse(SurveyResponse response) {
        try {
            restClient.post()
                    .uri("/api/v1/survey-read/response")
                    .body(response)
                    .retrieve()
                    .toBodilessEntity();
        } catch (Exception e) {
            log.error("Error saving response to read module", e);
        }
    }
}

