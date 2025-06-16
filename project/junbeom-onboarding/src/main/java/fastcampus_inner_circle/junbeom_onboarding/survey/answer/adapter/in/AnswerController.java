package fastcampus_inner_circle.junbeom_onboarding.survey.answer.adapter.in;

import fastcampus_inner_circle.junbeom_onboarding.survey.answer.adapter.in.dto.AnswerRequest;
import fastcampus_inner_circle.junbeom_onboarding.survey.answer.adapter.in.dto.AnswerResponse;
import fastcampus_inner_circle.junbeom_onboarding.survey.answer.application.service.AnswerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/answers")
@RequiredArgsConstructor
public class AnswerController {
    private final AnswerService answerService;

    @PostMapping
    public ResponseEntity<AnswerResponse> submitAnswer(@RequestBody AnswerRequest request) {
        AnswerResponse result = answerService.submitAnswer(request);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{formId}")
    public ResponseEntity<List<AnswerResponse>> getAnswers(@PathVariable Long formId) {
        List<AnswerResponse> answers = answerService.getAnswers(formId);
        return ResponseEntity.ok(answers);
    }
} 