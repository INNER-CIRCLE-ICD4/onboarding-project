package com.multi.sungwoongonboarding.forms.infrastructure;

import com.multi.sungwoongonboarding.forms.application.repository.FormRepository;
import com.multi.sungwoongonboarding.forms.domain.Forms;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FormRepositoryImpl implements FormRepository {

    private final FormJpaRepository formJpaRepository;

    @Override
    @Transactional
    public Forms save(Forms forms) {

        FormsJpaEntity formsJpaEntity = FormsJpaEntity.fromDomain(forms);
        formJpaRepository.save(formsJpaEntity);

        return formsJpaEntity.toDomain();
    }

    @Override
    public Forms findById(Long id) {

        FormsJpaEntity formsJpaEntity = formJpaRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Form not found with id: " + id));

        return formsJpaEntity.toDomain();
    }

    @Override
    public List<Forms> findAll() {
        return formJpaRepository.findAll().stream().map(FormsJpaEntity::toDomain).toList();
    }

    @Override
    @Transactional
    public Forms update(Long formId, Forms forms) {

        FormsJpaEntity existingForm = formJpaRepository.findById(formId)
                .orElseThrow(() -> new IllegalArgumentException("Form not found with id: " + formId));

        existingForm.update(forms);

        return existingForm.toDomain();
    }
}
