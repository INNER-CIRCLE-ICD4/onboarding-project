package com.innercircle.onboarding.form;

import com.fasterxml.jackson.databind.JsonNode;
import com.innercircle.onboarding.common.response.ApiResponse;
import com.innercircle.onboarding.form.domain.FormDto;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/form")
public class FormController {

    private final FormService formService;

    public FormController(FormService formService) {
        this.formService = formService;
    }

    @GetMapping("/list")
    public ApiResponse.Base<JsonNode> getFormList() {
        return ApiResponse.Base.success();
    }

    @GetMapping("/{uuid}")
    public ApiResponse.Base<JsonNode> get(@PathVariable String uuid) {
        return ApiResponse.Base.success();
    }

    @PostMapping
    public ApiResponse.Base<JsonNode> create(@RequestBody @Valid FormDto.Create createParam) {
        formService.create(createParam);
        return ApiResponse.Base.success();
    }

}
