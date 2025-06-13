package com.multi.sungwoongonboarding.forms.application;

import com.multi.sungwoongonboarding.forms.application.repository.FormRepository;
import com.multi.sungwoongonboarding.forms.domain.Forms;
import com.multi.sungwoongonboarding.forms.dto.FormCreateRequest;
import com.multi.sungwoongonboarding.forms.dto.FormResponse;
import com.multi.sungwoongonboarding.forms.dto.FormUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FormService {

    private final FormRepository formRepository;

    @Transactional
    public FormResponse createForms(FormCreateRequest formCreateRequest) {

        Forms createRequestDomain = formCreateRequest.toDomain();

        return FormResponse.fromDomain(
                formRepository.save(createRequestDomain)
        );
    }

    @Transactional
    public FormResponse updateForms(Long formId, FormUpdateRequest formUpdateRequest) {

        Forms updateRequestDomain = formUpdateRequest.toDomain();

        return FormResponse.fromDomain(
                formRepository.update(formId, updateRequestDomain)
        );

    }
}
