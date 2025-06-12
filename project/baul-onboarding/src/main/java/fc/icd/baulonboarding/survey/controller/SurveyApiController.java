package fc.icd.baulonboarding.survey.controller;

import fc.icd.baulonboarding.survey.model.dto.SurveyDto;
import fc.icd.baulonboarding.survey.service.SurveyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/api/surveys")
public class SurveyApiController {

    private final SurveyService surveyService;

    @PostMapping
    public void registerSurvey(@RequestBody SurveyDto.RegisterSurveyRequest request){
        surveyService.registerSurvey(request);
    }

    @PutMapping
    public void 설문조사수정API(){

    }


}
