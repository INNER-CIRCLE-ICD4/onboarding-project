package com.example.demo.answer.controller;


import com.example.demo.answer.domain.dto.CreateAnswerDto;
import com.example.demo.answer.service.AnswerService;
import com.example.demo.survey.domain.Survey;
import com.example.demo.survey.service.SurveyService;
import com.example.demo.utill.ApiResponseTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/answer")
public class AnswerController {

    private final SurveyService surveyService;

    private final AnswerService answerService;

    //응답 등록 api
    @PostMapping("/create/{surveyId}")
    public ApiResponseTemplate<?> createAnswer(
            @PathVariable Long surveyId,
            @Validated @RequestBody CreateAnswerDto createSurveyDTO
    ) {
        Survey survey  = surveyService.findSurvey(surveyId);
        return new ApiResponseTemplate<>(answerService.createAnswer(survey,createSurveyDTO), HttpStatus.OK);
    }




}
