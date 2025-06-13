package com.multi.sungwoongonboarding.forms.presentation;

import com.multi.sungwoongonboarding.common.dto.ResponseDto;
import com.multi.sungwoongonboarding.common.util.ResponseUtil;
import com.multi.sungwoongonboarding.forms.application.FormService;
import com.multi.sungwoongonboarding.forms.dto.FormCreateRequest;
import com.multi.sungwoongonboarding.forms.dto.FormResponse;
import com.multi.sungwoongonboarding.forms.dto.FormUpdateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/forms")
public class FormController {

    private final FormService formService;

    @PostMapping
    public ResponseDto<FormResponse> createForm(@RequestBody @Valid FormCreateRequest formCreateRequest) {
        FormResponse formResponse = formService.createForms(formCreateRequest);
        return ResponseUtil.success(formResponse);
    }

    @PatchMapping("/{formId}")
    public ResponseDto<FormResponse> updateForm(@PathVariable Long formId, @RequestBody @Valid FormUpdateRequest formUpdateRequest) {
        FormResponse formResponse = formService.updateForms(formId, formUpdateRequest);
        return ResponseUtil.success(formResponse);
    }
}
