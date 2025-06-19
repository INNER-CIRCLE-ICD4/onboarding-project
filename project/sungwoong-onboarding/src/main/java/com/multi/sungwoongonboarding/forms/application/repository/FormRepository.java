package com.multi.sungwoongonboarding.forms.application.repository;

import com.multi.sungwoongonboarding.forms.domain.Forms;

import java.util.List;

public interface FormRepository {

    Forms save(Forms forms);

    Forms findById(Long id);

    List<Forms> findAll();


    Forms update(Long formId, Forms forms);

}
