package com.tommy.jaeyoungonboarding.controller;

import com.tommy.jaeyoungonboarding.dto.CreateSurveyDTO;
import com.tommy.jaeyoungonboarding.entity.Survey;
import com.tommy.jaeyoungonboarding.service.SurveyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/survey")
@Slf4j
public class SurveyController {

    private final SurveyService surveyService;

    /*
    * 설문조사 전체 조회 - GET
    * @return 설문조사 리스트
    * */
    @GetMapping
    public ResponseEntity<?> selectAllSurvey(){

        List<Survey> selectAllSurvey = surveyService.selectAllSurvey();
        return ResponseEntity.ok(selectAllSurvey);
    }

    /*
    * 설문조사 생성 - POST
    * @return 생성 완료 메시지
    * */
    @PostMapping
    public ResponseEntity<?> createSurvey(@RequestBody CreateSurveyDTO createSurveyDTO){

        String surveyCreate = surveyService.createSurvey(createSurveyDTO);
        return ResponseEntity.ok("Create success survey: "  + surveyCreate);
    }
}
