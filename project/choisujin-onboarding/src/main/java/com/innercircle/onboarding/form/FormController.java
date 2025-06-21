package com.innercircle.onboarding.form;

import com.fasterxml.jackson.databind.JsonNode;
import com.innercircle.onboarding.common.response.ApiResponse;
import com.innercircle.onboarding.form.domain.Form;
import com.innercircle.onboarding.form.domain.FormDto;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/form")
public class FormController {

    private final FormService formService;

    public FormController(FormService formService) {
        this.formService = formService;
    }

    @GetMapping("/list")
    public ApiResponse.Base<List<Form>> getFormList() {
        return ApiResponse.Base.success(formService.getFormList());
    }

    @GetMapping("/{uuid}")
    public ApiResponse.Base<List<FormDto.SearchQuestionsDto>> get(@PathVariable UUID uuid, @RequestParam(required = false) String keyword) {
        return ApiResponse.Base.success(formService.getForm(uuid, keyword));
    }

    @PostMapping
    public ApiResponse.Base<JsonNode> create(@RequestBody @Valid FormDto.Create createParam) {
        formService.create(createParam);
        return ApiResponse.Base.success();
    }

}
