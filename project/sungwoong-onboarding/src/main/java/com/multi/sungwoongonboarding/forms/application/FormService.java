package com.multi.sungwoongonboarding.forms.application;

import com.multi.sungwoongonboarding.forms.application.repository.FormRepository;
import com.multi.sungwoongonboarding.forms.domain.Forms;
import com.multi.sungwoongonboarding.forms.dto.FormCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FormService {

    private final FormRepository formRepository;

    @Transactional
    public void createForms(FormCreateRequest formCreateRequest) {
        Forms form = formCreateRequest.toDomain();
        formRepository.save(form);
    }
}
