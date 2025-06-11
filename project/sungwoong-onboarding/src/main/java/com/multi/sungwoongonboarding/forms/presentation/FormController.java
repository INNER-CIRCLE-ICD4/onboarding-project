package com.multi.sungwoongonboarding.forms.presentation;

import com.multi.sungwoongonboarding.common.dto.ResponseDto;
import com.multi.sungwoongonboarding.common.util.ResponseUtil;
import com.multi.sungwoongonboarding.forms.application.FormService;
import com.multi.sungwoongonboarding.forms.dto.FormCreateRequest;
import com.multi.sungwoongonboarding.forms.dto.FormCreateResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/forms")
public class FormController {

    private final FormService formService;

    @PostMapping
    public ResponseDto<FormCreateResponse> createForm(@RequestBody @Valid FormCreateRequest formCreateRequest) {
        FormCreateResponse formCreateResponse = formService.createForms(formCreateRequest);
        return ResponseUtil.success(formCreateResponse);
    }
}
