package fc.icd.jaehyeononboarding.answer.controller;

import fc.icd.jaehyeononboarding.answer.model.dto.AnswerCreateDTO;
import fc.icd.jaehyeononboarding.answer.model.dto.AnswerRetrieveDTO;
import fc.icd.jaehyeononboarding.answer.service.AnswerService;
import fc.icd.jaehyeononboarding.common.model.ApiResponse;
import fc.icd.jaehyeononboarding.common.model.NoDataResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@RestController
public class AnswerRestController {
    private final AnswerService answerService;

    @PostMapping("/v1/surveys/{surveyId}/answers")
    public ResponseEntity<NoDataResponse> createAnswer(@PathVariable Long surveyId, @RequestBody AnswerCreateDTO dto) {
        return ResponseEntity.ok().body(answerService.createAnswer(surveyId, dto));
    }

    @GetMapping("/v1/surveys/{surveyId}/answers")
    public ResponseEntity<ApiResponse<List<AnswerRetrieveDTO>>> getAnswer(@PathVariable Long surveyId) {
        return ResponseEntity.ok().body(answerService.getAnswers(surveyId));
    }
}
