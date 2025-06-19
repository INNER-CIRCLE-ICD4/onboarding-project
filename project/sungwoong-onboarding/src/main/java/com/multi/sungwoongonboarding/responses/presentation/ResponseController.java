package com.multi.sungwoongonboarding.responses.presentation;

import com.multi.sungwoongonboarding.common.dto.ResponseDto;
import com.multi.sungwoongonboarding.common.util.ResponseUtil;
import com.multi.sungwoongonboarding.responses.application.ResponseService;
import com.multi.sungwoongonboarding.responses.dto.ResponseCreateRequest;
import com.multi.sungwoongonboarding.responses.dto.ResponseSheet;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/responses")
public class ResponseController {

    private final ResponseService responseService;

    @PostMapping
    public ResponseDto<ResponseSheet> submitResponse(@RequestBody @Valid ResponseCreateRequest responseCreateRequest) {
        ResponseSheet responseSheet = responseService.submitResponse(responseCreateRequest);
        return ResponseUtil.success(responseSheet);
    }
}