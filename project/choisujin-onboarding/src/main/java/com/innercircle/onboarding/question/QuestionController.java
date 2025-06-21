package com.innercircle.onboarding.question;

import com.fasterxml.jackson.databind.JsonNode;
import com.innercircle.onboarding.common.response.ApiResponse;
import com.innercircle.onboarding.question.domain.QuestionDto;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/question")
public class QuestionController {

    private final QuestionService questionService;

    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @PutMapping("/{seq}")
    public ApiResponse.Base<JsonNode> update(@PathVariable Long seq, @RequestBody @Valid QuestionDto.Update updateParam) {
        questionService.update(seq, updateParam);
        return ApiResponse.Base.success();
    }

}
