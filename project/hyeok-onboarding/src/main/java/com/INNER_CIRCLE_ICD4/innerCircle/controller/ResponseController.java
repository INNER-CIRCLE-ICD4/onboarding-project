package com.INNER_CIRCLE_ICD4.innerCircle.controller;

import com.INNER_CIRCLE_ICD4.innerCircle.dto.ResponseDto;
import com.INNER_CIRCLE_ICD4.innerCircle.dto.ResponseRequest;
import com.INNER_CIRCLE_ICD4.innerCircle.service.ResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/responses")
@RequiredArgsConstructor
public class ResponseController {

    private final ResponseService responseService;

    // ✅ 응답 저장
    @PostMapping
    public ResponseEntity<UUID> submitResponse(@RequestBody ResponseRequest request) {
        UUID responseId = responseService.saveResponse(request);
        return ResponseEntity.ok(responseId);
    }

    // ✅ 특정 설문 응답 전체 조회
    @GetMapping("/survey/{surveyId}")
    public ResponseEntity<List<ResponseDto>> getResponsesBySurvey(@PathVariable UUID surveyId) {
        List<ResponseDto> responses = responseService.findAllBySurveyId(surveyId);
        return ResponseEntity.ok(responses);
    }

    // ✅ 응답 ID로 상세 조회
    @GetMapping("/{responseId}")
    public ResponseEntity<ResponseDto> getResponseById(@PathVariable UUID responseId) {
        ResponseDto responseDto = responseService.findById(responseId);
        return ResponseEntity.ok(responseDto);
    }
}
