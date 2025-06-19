package fc.icd.baulonboarding.survey.controller;

import fc.icd.baulonboarding.common.reponse.CommonResponse;
import fc.icd.baulonboarding.survey.model.dto.SurveyCommand;
import fc.icd.baulonboarding.survey.model.dto.SurveyDto;
import fc.icd.baulonboarding.survey.model.mapper.SurveyDtoMapper;
import fc.icd.baulonboarding.survey.service.SurveyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/api/surveys")
public class SurveyApiController {

    private final SurveyService surveyService;
    private final SurveyDtoMapper surveyDtoMapper;

    @PostMapping
    public CommonResponse registerSurvey(@RequestBody @Valid SurveyDto.RegisterSurveyRequest request){
        SurveyCommand.RegisterSurvey surveyCommand = surveyDtoMapper.of(request);
        surveyService.registerSurvey(surveyCommand);
        return CommonResponse.success("ok");
    }

    @PutMapping
    public CommonResponse updateSurvey(@RequestBody @Valid SurveyDto.UpdateSurveyRequest request){
        SurveyCommand.UpdateSurvey surveyCommand = surveyDtoMapper.of(request);
        surveyService.updateSurvey(surveyCommand);
        return CommonResponse.success("ok");
    }


}
