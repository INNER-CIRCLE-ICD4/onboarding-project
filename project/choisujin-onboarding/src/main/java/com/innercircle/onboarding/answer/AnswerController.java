package com.innercircle.onboarding.answer;

import com.fasterxml.jackson.databind.JsonNode;
import com.innercircle.onboarding.answer.domain.AnswerDto;
import com.innercircle.onboarding.common.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/answer")
public class AnswerController {

    private final AnswerService answerService;

    public AnswerController(AnswerService answerService) {
        this.answerService = answerService;
    }

    @PostMapping
    public ApiResponse.Base<JsonNode> create(@RequestBody @Valid List<AnswerDto.Create> createAnswer) {
        answerService.create(createAnswer);
        return ApiResponse.Base.success();
    }

}
