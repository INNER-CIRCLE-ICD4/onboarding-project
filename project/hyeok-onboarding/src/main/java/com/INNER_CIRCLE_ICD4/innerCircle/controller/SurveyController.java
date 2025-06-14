package com.INNER_CIRCLE_ICD4.innerCircle.controller;

import com.INNER_CIRCLE_ICD4.innerCircle.dto.SurveyRequest;
import com.INNER_CIRCLE_ICD4.innerCircle.dto.SurveyResponse;
import com.INNER_CIRCLE_ICD4.innerCircle.dto.SurveyUpdateRequest;
import com.INNER_CIRCLE_ICD4.innerCircle.service.SurveyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/surveys")
@RequiredArgsConstructor
public class SurveyController {

    private final SurveyService surveyService;

    // ─── 1) 설문 생성 ────────────────────────────────────────────────────────────
    @PostMapping
    public ResponseEntity<SurveyResponse> createSurvey(
            @Valid @RequestBody SurveyRequest request) {
        SurveyResponse created = surveyService.createSurvey(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(created);
    }

    // ─── 2) 전체 설문 조회 ───────────────────────────────────────────────────────
    @GetMapping
    public ResponseEntity<List<SurveyResponse>> getAllSurveys() {
        return ResponseEntity.ok(surveyService.findAll());
    }

    // ─── 3) 단건 설문 조회 ───────────────────────────────────────────────────────
    @GetMapping("/{id}")
    public ResponseEntity<SurveyResponse> getSurveyById(@PathVariable UUID id) {
        return ResponseEntity.ok(surveyService.findById(id));
    }

    // ─── 4) 설문 수정 ────────────────────────────────────────────────────────────
    @PutMapping("/{id}")
    public ResponseEntity<SurveyResponse> updateSurvey(
            @PathVariable UUID id,
            @Valid @RequestBody SurveyUpdateRequest request
    ) {
        SurveyResponse updated = surveyService.updateSurvey(id, request);
        return ResponseEntity.ok(updated);
    }
}
