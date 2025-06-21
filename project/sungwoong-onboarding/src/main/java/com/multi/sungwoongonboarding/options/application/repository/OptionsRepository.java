package com.multi.sungwoongonboarding.options.application.repository;

import com.multi.sungwoongonboarding.options.domain.Options;

import java.util.Map;

public interface OptionsRepository {
    Options findById(Long id);

    Map<Long, Options> getOptionMapByFormId(Long formId);
}
