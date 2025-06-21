package com.innercircle.onboarding.changzune_onboarding.survey.controller;

import com.innercircle.onboarding.changzune_onboarding.survey.dto.AnswerRequest;
import com.innercircle.onboarding.changzune_onboarding.survey.dto.AnswerResponse;
import com.innercircle.onboarding.changzune_onboarding.survey.service.AnswerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // 이 클래스가 REST API 컨트롤러임을 명시 (자동으로 @ResponseBody 적용)
@RequestMapping("/api/answers") // 이 컨트롤러의 기본 URL 경로를 설정 (예: /api/answers)
@RequiredArgsConstructor // final 필드를 자동으로 생성자로 만들어주는 Lombok 애노테이션
public class AnswerController {

    private final AnswerService answerService; // 응답 저장 등의 로직을 담당하는 서비스 객체

    //설문 응답제출
    @PostMapping // HTTP POST 요청이 들어왔을 때 실행될 메서드임을 명시
    public ResponseEntity<String> submitAnswer(@RequestBody AnswerRequest requestDto) {
        // 클라이언트가 보낸 요청 바디를 AnswerRequestDto로 파싱해 매개변수로 받음
        answerService.submitAnswer(requestDto); // 서비스 레이어를 호출하여 응답 저장 수행
        return ResponseEntity.ok("응답이 저장되었습니다."); // 상태 코드 200(OK)와 메시지를 응답
    }

    //설문 응답 조회
    @GetMapping("/{id}/answers")
    public ResponseEntity<List<AnswerResponse>> getAnswers(
            @PathVariable Long id,
            @RequestParam(required = false) String questionName,
            @RequestParam(required = false) String answerValue) {

        List<AnswerResponse> results = answerService.getAnswers(id, questionName, answerValue);
        return ResponseEntity.ok(results);
    }

}