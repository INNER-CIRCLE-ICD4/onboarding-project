package com.innercircle.onboarding.changzune_onboarding.survey.controller;

import com.innercircle.onboarding.changzune_onboarding.survey.domain.Survey;
import com.innercircle.onboarding.changzune_onboarding.survey.dto.SurveyListResponse;
import com.innercircle.onboarding.changzune_onboarding.survey.dto.SurveyRequest;
import com.innercircle.onboarding.changzune_onboarding.survey.dto.SurveyResponse;
import com.innercircle.onboarding.changzune_onboarding.survey.dto.SurveyUpdateRequest;
import com.innercircle.onboarding.changzune_onboarding.survey.service.SurveyService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/surveys")
@RequiredArgsConstructor
public class SurveyController {

    private final SurveyService surveyService;

    // 설문 생성 API: POST /api/surveys 호출을 하면 갑니다.
    // 포스팅 맵핍으로
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Survey> createSurvey(@RequestBody @Valid SurveyRequest request) {

        //@RequestBody jSON 객체 받아서 자바객체로 변환
        //@Valid 유효성검사 체크하는것인데 유효하지않으면 400으로 던짐
        Survey savedSurvey = surveyService.createSurvey(request);

        //HTTP 응답을 감싸는 클래스 상태코드도 전송
        return new ResponseEntity<>(savedSurvey, HttpStatus.CREATED);
    }

    //설문 수정 API 만들기

    @PutMapping("/{id}")
    public ResponseEntity<String> updateSurvey(
            @PathVariable Long id,
            @RequestBody SurveyUpdateRequest request
    ) {
        surveyService.updateSurvey(id, request);
        return ResponseEntity.ok("수정 완료");
    }

    //설문 단건 조회
    @GetMapping("/{id}")
    public ResponseEntity<SurveyResponse> getSurveyById(@PathVariable Long id) {
        SurveyResponse response = surveyService.getSurveyById(id);
        return ResponseEntity.ok(response);
    }

    //설문 다건 조회
    @GetMapping
    public ResponseEntity<List<SurveyListResponse>> getAllSurveys() {
        List<SurveyListResponse> response = surveyService.getAllSurveys();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSurvey(@PathVariable Long id) {
        surveyService.deleteSurvey(id);
        return ResponseEntity.ok("삭제 완료");
    }



}