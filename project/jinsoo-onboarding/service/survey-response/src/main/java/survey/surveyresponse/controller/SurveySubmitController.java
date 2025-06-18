package survey.surveyresponse.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import survey.surveyresponse.controller.request.SurveySubmitRequest;
import survey.surveyresponse.service.SurveySubmitService;
import survey.surveyresponse.service.response.SurveySubmitResponse;

@RestController
@RequestMapping("/api/v1/survey-response")
@RequiredArgsConstructor
public class SurveySubmitController {

    private final SurveySubmitService surveySubmitService;

    @PostMapping("/{surveyId}")
    public ResponseEntity<SurveySubmitResponse> submit(@PathVariable Long surveyId,
                                                       @RequestBody SurveySubmitRequest surveySubmitRequest) {
        SurveySubmitResponse response = surveySubmitService.submit(surveyId, surveySubmitRequest);
        return ResponseEntity.ok(response);
    }
}
