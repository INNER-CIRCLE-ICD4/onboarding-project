package survey.surveyresponse.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import survey.surveyresponse.controller.request.SurveySubmitRequest;
import survey.surveyresponse.service.SurveyResponseService;
import survey.surveyresponse.service.response.SurveySubmitResponse;

@RestController
@RequestMapping("/api/v1/survey-response")
@RequiredArgsConstructor
public class SurveyResponseController {

    private final SurveyResponseService surveyResponseService;

    @PostMapping("/{surveyId}")
    public ResponseEntity<SurveySubmitResponse> submit(@PathVariable Long surveyId,
                                                       @RequestBody SurveySubmitRequest surveySubmitRequest) {
        SurveySubmitResponse response = surveyResponseService.submit(surveyId, surveySubmitRequest);
        return ResponseEntity.ok(response);
    }
}
