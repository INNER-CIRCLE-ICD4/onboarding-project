package com.example.demo.survey.controller;


import com.example.demo.survey.Dto.CreateSurveyDTO;
import com.example.demo.survey.service.SurveyService;
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
    public ResponseEntity<?> createSurvey(
            @Validated @RequestBody CreateSurveyDTO createSurveyDTO
    ) {
        return new ResponseEntity(surveyService.create(createSurveyDTO));
    }

}
