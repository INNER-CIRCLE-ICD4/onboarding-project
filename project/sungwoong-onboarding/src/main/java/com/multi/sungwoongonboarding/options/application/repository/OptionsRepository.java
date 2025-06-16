package com.multi.sungwoongonboarding.options.application.repository;

import com.multi.sungwoongonboarding.options.domain.Options;

public interface OptionsRepository {
    Options findById(Long id);
}
