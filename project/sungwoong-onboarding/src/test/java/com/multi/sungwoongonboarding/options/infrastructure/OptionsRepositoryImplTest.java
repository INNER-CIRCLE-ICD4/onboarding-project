package com.multi.sungwoongonboarding.options.infrastructure;

import com.multi.sungwoongonboarding.options.application.repository.OptionsRepository;
import com.multi.sungwoongonboarding.options.domain.Options;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import java.util.Map;

@SpringBootTest
@ActiveProfiles("test")
@Sql(scripts = "/sql/form_insert_data.sql")
class OptionsRepositoryImplTest {

    @Autowired
    OptionsRepository optionsRepository;

    @Test
    @DisplayName("Options : formId에 해당하는 optionMap 조회")
    public void findByFormId() {

        //Given
        Long formId = 1L;

        //When
        Map<Long, Options> optionMapByFormId = optionsRepository.getOptionMapByFormId(formId);

        //Then
        Assertions.assertThat(optionMapByFormId).hasSize(5);

    }
}