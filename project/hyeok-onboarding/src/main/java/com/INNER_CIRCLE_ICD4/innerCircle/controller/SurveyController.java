package com.INNER_CIRCLE_ICD4.innerCircle.controller;

import com.INNER_CIRCLE_ICD4.innerCircle.dto.SurveyRequest;
import com.INNER_CIRCLE_ICD4.innerCircle.dto.SurveyResponse;
import com.INNER_CIRCLE_ICD4.innerCircle.service.SurveyService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/surveys")
@RequiredArgsConstructor
public class SurveyController {

    private final SurveyService surveyService;

    // 설문 생성: 201 Created + Location 헤더
    @PostMapping
    public ResponseEntity<SurveyResponse> createSurvey(
            @Valid @RequestBody SurveyRequest request) {
        SurveyResponse created = surveyService.createSurvey(request);
        URI location = URI.create("/surveys/" + created.id());
        return ResponseEntity
                .created(location)
                .body(created);
    }

    // 전체 설문 조회: 200 OK
    @GetMapping
    public ResponseEntity<List<SurveyResponse>> getAllSurveys() {
        List<SurveyResponse> list = surveyService.findAll();
        return ResponseEntity.ok(list);
    }

    // 단건 설문 조회: 200 OK
    @GetMapping("/{id}")
    public ResponseEntity<SurveyResponse> getSurveyById(
            @PathVariable UUID id) {
        SurveyResponse response = surveyService.findById(id);
        return ResponseEntity.ok(response);
    }
}
