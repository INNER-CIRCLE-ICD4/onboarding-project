package com.multi.sungwoongonboarding.forms.presentation;

import com.multi.sungwoongonboarding.forms.application.FormService;
import com.multi.sungwoongonboarding.forms.dto.FormCreateRequest;
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

    @PostMapping("/create")
    public void createForm(@RequestBody FormCreateRequest formCreateRequest) {

        formService.createForms(formCreateRequest);

    }

}
