package com.multi.sungwoongonboarding.forms.application.repository;

import com.multi.sungwoongonboarding.forms.domain.Forms;
import com.multi.sungwoongonboarding.forms.infrastructure.FormRepositoryImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;


@Import({FormRepositoryImpl.class})
@ExtendWith(SpringExtension.class)
@DataJpaTest
public class FormRepositoryTest {

    @Autowired
    FormRepository formRepository;

    @Test
    @DisplayName("Forms 엔티티가 저장되고 조회되는지 테스트")
    public void formSave() {

        // Given
        Forms formsDomain = Forms.builder()
                .title("테스트 폼")
                .description("테스트 폼 설명")
                .build();

        //When
        Forms savedForms = formRepository.save(formsDomain);
        List<Forms> all = formRepository.findAll();

        //Then
        Assertions.assertThat(all.size()).isEqualTo(1);
        Assertions.assertThat(all.get(0).getTitle()).isEqualTo("테스트 폼");

    }
}