package com.example.byeongjin_onboarding.controller;

import com.example.byeongjin_onboarding.dto.FormItemDto;
import com.example.byeongjin_onboarding.dto.SurveyCreateRequest;
import com.example.byeongjin_onboarding.dto.SurveyCreateResponse;
import com.example.byeongjin_onboarding.service.SurveyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/api/surveys")
public class SurveyApiController {

    @Autowired
    private SurveyService surveyService;

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
}