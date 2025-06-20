package com.multi.sungwoongonboarding.options.infrastructure;

import com.multi.sungwoongonboarding.options.application.repository.OptionsRepository;
import com.multi.sungwoongonboarding.options.domain.Options;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class OptionsRepositoryImpl implements OptionsRepository {

    private final OptionsJpaRepository optionsJpaRepository;

    @Override
    public Options findById(Long id) {
        OptionsJpaEntity optionsJpaEntity = optionsJpaRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Option not found with id: " + id));
        return optionsJpaEntity.toDomain();
    }

    @Override
    public Map<Long, Options> getOptionMapByFormId(Long formId) {

        return optionsJpaRepository.findByFormId(formId).stream().collect(Collectors.toMap(OptionsJpaEntity::getId, OptionsJpaEntity::toDomain));
    }
}
