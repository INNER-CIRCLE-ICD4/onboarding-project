package com.multi.sungwoongonboarding.forms.infrastructure;

import com.multi.sungwoongonboarding.forms.application.repository.FormRepository;
import com.multi.sungwoongonboarding.forms.domain.Forms;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class FormRepositoryImpl implements FormRepository {

    private final FormJpaRepository formJpaRepository;

    @Override
    public Forms save(Forms forms) {

        FormsJpaEntity formsJpaEntity = FormsJpaEntity.fromDomain(forms);
        formJpaRepository.save(formsJpaEntity);

        return formsJpaEntity.toDomain();
    }

    @Override
    public List<Forms> findAll() {
        return formJpaRepository.findAll().stream().map(FormsJpaEntity::toDomain).toList();
    }
}
