package com.example.demo.survey.controller;


import com.example.demo.survey.Dto.CreateSurveyDTO;
import com.example.demo.survey.Dto.UpdateSurveyDTO;
import com.example.demo.survey.service.SurveyService;
import com.example.demo.utill.ApiResponseTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/survey")
public class Controller {

    private final SurveyService surveyService;

    @PostMapping("/create")
    public ApiResponseTemplate<?> createSurvey(
            @Validated @RequestBody CreateSurveyDTO createSurveyDTO
    ) {
        return new ApiResponseTemplate<>(surveyService.create(createSurveyDTO),HttpStatus.OK);
    }

    @PutMapping("/update")
    public ApiResponseTemplate<?> updateSurvey(
            @Validated @RequestBody UpdateSurveyDTO updateSurveyDTO
    ) {
        return new ApiResponseTemplate<>(surveyService.update(updateSurveyDTO),HttpStatus.OK);
    }

}
