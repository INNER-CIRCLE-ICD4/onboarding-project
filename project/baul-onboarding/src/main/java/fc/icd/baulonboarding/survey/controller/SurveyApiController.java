package fc.icd.baulonboarding.survey.controller;

import fc.icd.baulonboarding.survey.model.dto.SurveyDto;
import fc.icd.baulonboarding.survey.service.SurveyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/api/surveys")
public class SurveyApiController {

    private final SurveyService surveyService;

    @PostMapping
    public void registerSurvey(@RequestBody @Valid SurveyDto.RegisterSurveyRequest request){
        surveyService.registerSurvey(request);
    }

    @PutMapping
    public void updateSurvey(@RequestBody @Valid SurveyDto.UpdateSurveyRequest request){
        surveyService.updateSurvey(request);

    }


}
