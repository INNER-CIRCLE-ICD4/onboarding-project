package survey.surveyread.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import survey.surveyread.service.SurveyReadService;
import survey.surveyread.service.response.SurveyReadResponse;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/survey-read")
public class SurveyReadController {
    private final SurveyReadService surveyReadService;

    @PostMapping("/response")
    public ResponseEntity<Void> saveResponse(@RequestBody SurveyReadResponse request) {
        surveyReadService.saveResponse(request);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/surveys/{surveyId}")
    public ResponseEntity<List<SurveyReadResponse>> getResponses(@PathVariable Long surveyId) {
        return ResponseEntity.ok(surveyReadService.getResponsesBySurveyId(surveyId));
    }
}
