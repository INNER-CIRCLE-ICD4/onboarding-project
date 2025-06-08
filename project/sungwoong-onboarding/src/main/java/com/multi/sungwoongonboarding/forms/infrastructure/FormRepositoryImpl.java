package com.multi.sungwoongonboarding.forms.infrastructure;

import com.multi.sungwoongonboarding.forms.application.repository.FormRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class FormRepositoryImpl implements FormRepository {

    private final FormJpaRepository formJpaRepository;

}
