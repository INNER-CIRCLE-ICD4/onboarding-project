package com.group.surveyapp.controller;

import com.group.surveyapp.service.SurveyService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.group.surveyapp.dto.SurveyAnswerRequestDto;
import com.group.surveyapp.dto.SurveyAnswerResponseDto;
import com.group.surveyapp.dto.SurveyCreateRequestDto;
import com.group.surveyapp.dto.SurveyResponseDto;
import com.group.surveyapp.dto.SurveyUpdateRequestDto;

import java.util.List;

/**
 * 설문조사 API 컨트롤러
 * <p>
 * - 설문 생성, 수정, 응답 제출, 응답 조회 기능 제공
 * </p>
 */
@RestController
@RequestMapping("/api/surveys")
public class SurveyController {

    private final SurveyService surveyService;

    public SurveyController(SurveyService surveyService) {
        this.surveyService = surveyService;
    }

    /**
     * 1. 설문조사 생성 API
     * <p>
     * - 클라이언트가 설문 이름, 설명, 질문 항목을 포함하여 설문을 생성
     * </p>
     * @param requestDto 설문조사 생성 요청 DTO
     * @return 생성된 설문 정보 DTO
     * @status 200 OK
     */
    @PostMapping
    public ResponseEntity<SurveyResponseDto> createSurvey(@RequestBody SurveyCreateRequestDto requestDto) {
        return ResponseEntity.ok(surveyService.createSurvey(requestDto));
    }

    /**
     * 2. 설문조사 수정 API
     * <p>
     * - 설문 ID를 통해 기존 설문을 수정
     * - 질문 항목 추가/삭제/변경 가능 (기존 응답은 보존)
     * </p>
     * HTTP Method : PUT
     * HTTP Path : /api/surveys/{surveyId}
     * @param surveyId 수정 대상 설문 ID
     * @param requestDto 수정 내용
     * @return 수정된 설문 정보 DTO
     * @status 200 OK
     * @throws IllegalArgumentException ID 존재하지 않을 경우
     */
    @PutMapping("/{surveyId}")
    public ResponseEntity<SurveyResponseDto> updateSurvey(@PathVariable Long surveyId, @RequestBody SurveyUpdateRequestDto requestDto) {
        return ResponseEntity.ok(surveyService.updateSurvey(surveyId, requestDto));
    }

    /**
     * 3. 설문 응답 제출 API
     * <p>
     * - 사용자가 설문에 응답을 제출
     * - 응답 항목은 설문 질문과 정확히 매칭되어야 함
     * </p>
     * HTTP Method : POST
     * HTTP Path : /api/surveys/{surveyId}/answers
     * @param surveyId 응답 대상 설문 ID
     * @param requestDto 응답 내용 DTO
     * @return 상태 코드 200 OK (본문 없음)
     * @throws IllegalArgumentException 질문 항목 불일치 시
     */
    @PostMapping("/{surveyId}/answers")
    public ResponseEntity<Void> submitAnswer(@PathVariable Long surveyId, @RequestBody SurveyAnswerRequestDto requestDto) {
        surveyService.submitAnswer(surveyId, requestDto);
        return ResponseEntity.ok().build();
    }

    /**
     * 4. 설문 응답 조회 API
     * <p>
     * - 특정 설문에 대한 모든 응답 내역을 조회
     * - Advanced: 항목 이름 및 응답 값 기반 검색은 서비스단에서 확장 가능
     * </p>
     * HTTP Method : GET
     * HTTP Path : /api/surveys/{surveyId}/answers
     * @param surveyId 응답 조회 대상 설문 ID
     * @return 응답 리스트
     * @status 200 OK
     */
    @GetMapping("/{surveyId}/answers")
    public ResponseEntity<List<SurveyAnswerResponseDto>> getAnswers(@PathVariable Long surveyId) {
        return ResponseEntity.ok(surveyService.getAnswers(surveyId));
    }

    /**
     * 예외 테스트용 엔드포인트
     * - IllegalArgumentException 발생 테스트
     */
    @GetMapping("/answers/error-test")
    public void errorTest() {
        throw new IllegalArgumentException("테스트 에외");
    }
}
