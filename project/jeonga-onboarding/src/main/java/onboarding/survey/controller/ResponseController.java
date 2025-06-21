package onboarding.survey.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import onboarding.survey.dto.SubmitResponseRequest;
import onboarding.survey.dto.SubmitResponseResponse;
import onboarding.survey.domain.SurveyResponse;
import onboarding.survey.service.ResponseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/surveys/{id}/response")
@RequiredArgsConstructor
public class ResponseController {

    private final ResponseService responseService;

    /**
     * 설문 응답 제출
     */
    @PostMapping
    public ResponseEntity<SubmitResponseResponse> submitResponse(
            @PathVariable Long id,
            @Valid @RequestBody SubmitResponseRequest request
    ) {
        SurveyResponse saved = responseService.submit(
                id,
                request.surveyItemId(),
                request.answer()
        );
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new SubmitResponseResponse(saved.getId()));
    }
}