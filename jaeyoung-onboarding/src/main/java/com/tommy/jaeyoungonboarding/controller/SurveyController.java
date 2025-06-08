package com.tommy.jaeyoungonboarding.controller;

import com.tommy.jaeyoungonboarding.entity.Survey;
import com.tommy.jaeyoungonboarding.service.SurveyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/survey")
public class SurveyController {

    private final SurveyService surveyService;

    /*
    * 설문조사 전체 조회 API - GET
    * @return 설문조사 리스트
    * */
    @GetMapping
    public ResponseEntity<?> selectAllSurvey(){
        List<Survey> selectAllSurvey = surveyService.selectAllSurvey();
        return ResponseEntity.ok(selectAllSurvey);
    }
}
