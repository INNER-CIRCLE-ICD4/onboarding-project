package com.multi.sungwoongonboarding.forms.application;

import com.multi.sungwoongonboarding.forms.application.repository.FormRepository;
import com.multi.sungwoongonboarding.forms.domain.Forms;
import com.multi.sungwoongonboarding.forms.dto.FormCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FormService {

    private final FormRepository formRepository;

    public void createForms(FormCreateRequest formCreateRequest) {

        Forms form = Forms.form(formCreateRequest);
        formRepository.save(form);

    }


}
