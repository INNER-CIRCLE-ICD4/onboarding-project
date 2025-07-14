package com.example.demo.survey.controller;


import com.example.demo.answer.service.AnswerService;
import com.example.demo.survey.domain.dto.CreateSurveyDTO;
import com.example.demo.survey.domain.dto.UpdateSurveyDTO;
import com.example.demo.survey.service.SurveyService;
import com.example.demo.utill.ApiResponseTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/survey")
public class SurveyController {

    private final SurveyService surveyService;

    private final AnswerService answerService;

    //설문조사 생성
    @PostMapping("/create")
    public ApiResponseTemplate<?> createSurvey(
            @Validated @RequestBody CreateSurveyDTO createSurveyDTO
    ) {
        return new ApiResponseTemplate<>(surveyService.create(createSurveyDTO),HttpStatus.OK);
    }

    //설문조사 수정
    @PutMapping("/update/{surveyId}")
    public ApiResponseTemplate<?> updateSurvey(
            @PathVariable Long surveyId,
            @Validated @RequestBody UpdateSurveyDTO updateSurveyDTO
    ) {
        return new ApiResponseTemplate<>(surveyService.update(surveyId,updateSurveyDTO),HttpStatus.OK);
    }

}
