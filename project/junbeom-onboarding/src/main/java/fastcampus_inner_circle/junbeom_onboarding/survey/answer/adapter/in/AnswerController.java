package fastcampus_inner_circle.junbeom_onboarding.survey.answer.adapter.in;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import fastcampus_inner_circle.junbeom_onboarding.survey.answer.adapter.in.dto.AnswerRequest;
import fastcampus_inner_circle.junbeom_onboarding.survey.answer.adapter.in.dto.AnswerResponse;
import fastcampus_inner_circle.junbeom_onboarding.survey.answer.application.service.AnswerService;
import lombok.RequiredArgsConstructor;

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

    @GetMapping("/{answerId}")
    public ResponseEntity<AnswerResponse> getAnswerById(@PathVariable Long answerId) {
        return answerService.getAnswerById(answerId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<AnswerResponse>> getAnswersByFormId(@RequestParam Long formId) {
        List<AnswerResponse> results = answerService.getAnswersByFormId(formId);
        return ResponseEntity.ok(results);
    }
} 