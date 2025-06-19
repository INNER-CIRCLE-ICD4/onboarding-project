package fc.icd.baulonboarding.survey.controller;

import fc.icd.baulonboarding.common.reponse.CommonResponse;
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
    public CommonResponse registerSurvey(@RequestBody @Valid SurveyDto.RegisterSurveyRequest request){
        surveyService.registerSurvey(request);
        return CommonResponse.success("ok");
    }

    @PutMapping
    public CommonResponse updateSurvey(@RequestBody @Valid SurveyDto.UpdateSurveyRequest request){
        surveyService.updateSurvey(request);
        return CommonResponse.success("ok");
    }


}
