package com.INNER_CIRCLE_ICD4.innerCircle.controller;

import com.INNER_CIRCLE_ICD4.innerCircle.domain.Survey;
import com.INNER_CIRCLE_ICD4.innerCircle.dto.SurveyRequest;
import com.INNER_CIRCLE_ICD4.innerCircle.dto.SurveyResponse;
import com.INNER_CIRCLE_ICD4.innerCircle.service.SurveyService;
import com.INNER_CIRCLE_ICD4.innerCircle.mapper.SurveyMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/surveys")
@RequiredArgsConstructor
public class SurveyController {

    private final SurveyService surveyService;

    // 🔍 모든 설문 조회
    @GetMapping
    public List<SurveyResponse> getAllSurveys() {
        return surveyService.findAll();
    }

    // 🔍 단일 설문 조회
    @GetMapping("/{id}")
    public SurveyResponse getSurveyById(@PathVariable UUID id) {
        return surveyService.findById(id);
    }

    // ✅ 설문 생성
    @PostMapping
    public ResponseEntity<SurveyResponse> createSurvey(@RequestBody SurveyRequest request) {
        Survey created = surveyService.createSurvey(request);
        return ResponseEntity.ok(SurveyMapper.toResponse(created)); // Survey → DTO 변환
    }
}
