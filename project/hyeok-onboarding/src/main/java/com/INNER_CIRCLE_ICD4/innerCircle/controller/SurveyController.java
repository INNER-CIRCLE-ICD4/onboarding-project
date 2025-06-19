package com.INNER_CIRCLE_ICD4.innerCircle.controller;

import com.INNER_CIRCLE_ICD4.innerCircle.dto.SurveyRequest;
import com.INNER_CIRCLE_ICD4.innerCircle.dto.SurveyResponse;
import com.INNER_CIRCLE_ICD4.innerCircle.dto.SurveyUpdateRequest;
import com.INNER_CIRCLE_ICD4.innerCircle.service.SurveyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/surveys")
@RequiredArgsConstructor
public class SurveyController {

    private final SurveyService surveyService;

    // 설문 생성
    @PostMapping
    public ResponseEntity<SurveyResponse> createSurvey(@RequestBody SurveyRequest request) {
        SurveyResponse created = surveyService.createSurvey(request);
        return ResponseEntity
                .created(URI.create("/surveys/" + created.id()))
                .body(created);
    }

    // 설문 단건 조회
    @GetMapping("/{id}")
    public ResponseEntity<SurveyResponse> getSurvey(@PathVariable UUID id) {
        return ResponseEntity.ok(surveyService.findById(id));
    }

    // 설문 수정
    @PutMapping("/{id}")
    public ResponseEntity<SurveyResponse> updateSurvey(@PathVariable UUID id,
                                                       @RequestBody SurveyUpdateRequest request) {
        SurveyResponse updated = surveyService.update(id, request);
        return ResponseEntity.ok(updated);
    }
}
