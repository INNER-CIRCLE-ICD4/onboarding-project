package com.multi.sungwoongonboarding.forms.application;

import com.multi.sungwoongonboarding.forms.application.repository.FormRepository;
import com.multi.sungwoongonboarding.forms.domain.Forms;
import com.multi.sungwoongonboarding.forms.dto.FormCreateRequest;
import com.multi.sungwoongonboarding.forms.dto.FormCreateResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FormService {

    private final FormRepository formRepository;

    @Transactional
    public FormCreateResponse createForms(FormCreateRequest formCreateRequest) {
        Forms requestDomain = formCreateRequest.toDomain();
        Forms responseDomain = formRepository.save(requestDomain);
        return FormCreateResponse.fromDomain(responseDomain);
    }
}
