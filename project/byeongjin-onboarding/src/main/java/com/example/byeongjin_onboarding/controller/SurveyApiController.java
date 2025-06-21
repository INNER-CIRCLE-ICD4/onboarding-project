package com.example.byeongjin_onboarding.controller;

import com.example.byeongjin_onboarding.dto.FormItemDto;
import com.example.byeongjin_onboarding.dto.SubmitAnswerRequest;
import com.example.byeongjin_onboarding.dto.SurveyCreateRequest;
import com.example.byeongjin_onboarding.dto.SurveyCreateResponse;
import com.example.byeongjin_onboarding.service.SurveyAnswerService;
import com.example.byeongjin_onboarding.service.SurveyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.NoSuchElementException;
import java.util.Objects;

@RestController
@RequestMapping("/api/surveys")
public class SurveyApiController {

    @Autowired
    private SurveyService surveyService;
    @Autowired
    private SurveyAnswerService surveyAnswerService;

    // 설문조사 생성 (API)
    @PostMapping("/createSurvey")
    public ResponseEntity<?> createSurvey(@RequestBody SurveyCreateRequest request) {
        if (request.getName() == null || request.getName().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("설문 제목을 입력해주세요.");
        }

        if (Objects.isNull(request.getFormItems()) || request.getFormItems().isEmpty()) {
            return ResponseEntity.badRequest().body("설문 항목은 최소 1개 이상이어야 합니다.");
        }

        for (FormItemDto item : request.getFormItems()) {
            if (item.getName() == null || item.getName().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("질문 내용은 비어있을 수 없습니다.");
            }

            if (item.getItemType() == null || item.getItemType().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("질문 유형은 필수입니다.");
            }


            if ("SINGLE_CHOICE".equals(item.getItemType()) || "MULTI_CHOICE".equals(item.getItemType())) {
                if (Objects.isNull(item.getOptions()) || item.getOptions().isEmpty()) {
                    return ResponseEntity.badRequest().body("선택형 질문은 최소 1개 이상의 옵션이 필요합니다.");
                }
                for (String optionText : item.getOptions()) {
                    if (optionText == null || optionText.trim().isEmpty()) {
                        return ResponseEntity.badRequest().body("옵션 내용은 비어있을 수 없습니다.");
                    }
                }
            }
        }

        try {
            SurveyCreateResponse createdSurvey = surveyService.createSurvey(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdSurvey);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("설문 저장 중 서버 오류가 발생했습니다: " + e.getMessage());
        }
    }

    // 특정 설문조사 ID로 조회 (API)
    @GetMapping("/createSurvey/{id}")
    public ResponseEntity<SurveyCreateResponse> getSurveyById(@PathVariable Long id) {
        try {
            SurveyCreateResponse survey = surveyService.getSurveyById(id);
            return ResponseEntity.ok(survey);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // 설문조사 수정 (API)
    @PutMapping("/createSurvey/{id}")
    public ResponseEntity<SurveyCreateResponse> updateSurvey(@PathVariable Long id, @RequestBody SurveyCreateRequest request) {
        try {
            SurveyCreateResponse updatedSurvey = surveyService.updateSurvey(id, request);
            return ResponseEntity.ok(updatedSurvey);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // 설문조사 삭제 (API)
    @DeleteMapping("/createSurvey/{id}")
    public ResponseEntity<Void> deleteSurvey(@PathVariable Long id) {
        try {
            surveyService.deleteSurvey(id);
            return ResponseEntity.noContent().build(); // 204 No Content
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404 Not Found
        }
    }

    // 설문 응답 제출 (API)
    @PostMapping("/submit-answer")
    public ResponseEntity<String> submitAnswer(@RequestBody SubmitAnswerRequest request) {
        try {
            String responseMessage = surveyAnswerService.submitAnswer(request);
            return ResponseEntity.ok(responseMessage);
        } catch (NoSuchElementException | IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("응답 제출 중 오류 발생: " + e.getMessage());
        }
    }


}