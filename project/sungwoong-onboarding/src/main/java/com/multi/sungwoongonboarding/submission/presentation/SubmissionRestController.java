package com.multi.sungwoongonboarding.submission.presentation;

import com.multi.sungwoongonboarding.common.dto.ResponseDto;
import com.multi.sungwoongonboarding.common.util.ResponseUtil;
import com.multi.sungwoongonboarding.submission.application.SubmissionService;
import com.multi.sungwoongonboarding.submission.dto.SubmissionCreateRequest;
import com.multi.sungwoongonboarding.submission.dto.SubmissionResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/submission")
public class SubmissionRestController {

    private final SubmissionService submissionService;

    @GetMapping("/{formId}")
    public ResponseDto<List<SubmissionResponse>> searchSubmission(@PathVariable Long formId,
                                                                  @RequestParam(required = false) String questionText,
                                                                  @RequestParam(required = false) String answerText) {
        List<SubmissionResponse> submissionByFormId = submissionService.getSubmissionByFormId(formId, questionText, answerText);
        return ResponseUtil.success(submissionByFormId);
    }

    @PostMapping
    public ResponseDto<Long> submitResponse(@RequestBody @Valid SubmissionCreateRequest submissionCreateRequest) {
        Long submissionResponse = submissionService.submitResponse(submissionCreateRequest);
        return ResponseUtil.success(submissionResponse);
    }
}