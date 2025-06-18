package com.multi.sungwoongonboarding.submission.presentation;

import com.multi.sungwoongonboarding.common.dto.ResponseDto;
import com.multi.sungwoongonboarding.common.util.ResponseUtil;
import com.multi.sungwoongonboarding.submission.application.SubmissionService;
import com.multi.sungwoongonboarding.submission.dto.SubmissionCreateRequest;
import com.multi.sungwoongonboarding.submission.dto.SubmissionResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/responses")
public class SubmissionRestController {

    private final SubmissionService submissionService;

    @PostMapping
    public ResponseDto<SubmissionResponse> submitResponse(@RequestBody @Valid SubmissionCreateRequest submissionCreateRequest) {
        SubmissionResponse submissionResponse = submissionService.submitResponse(submissionCreateRequest);
        return ResponseUtil.success(submissionResponse);
    }
}