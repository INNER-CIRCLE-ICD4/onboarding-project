package fc.icd.jaehyeononboarding.survey.controller;

import fc.icd.jaehyeononboarding.common.model.NoDataResponse;
import fc.icd.jaehyeononboarding.survey.model.dto.SurveyCreateDTO;
import fc.icd.jaehyeononboarding.survey.model.dto.SurveyUpdateDTO;
import fc.icd.jaehyeononboarding.survey.service.SurveyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RestController
public class SurveyRestController {

    private final SurveyService surveyService;

    @PostMapping("/v1/surveys")
    public ResponseEntity<NoDataResponse> createSurvey(@RequestBody SurveyCreateDTO dto) {

        return ResponseEntity.ok().body(surveyService.createSurvey(dto));
    }

    @PutMapping("/v1/surveys/{surveyId}")
    public ResponseEntity<NoDataResponse> updateSurvey(@RequestBody SurveyUpdateDTO dto, @PathVariable Long surveyId) {
        dto.setSurveyId(surveyId);
        return ResponseEntity.ok().body(surveyService.updateSurvey(dto));
    }

}
