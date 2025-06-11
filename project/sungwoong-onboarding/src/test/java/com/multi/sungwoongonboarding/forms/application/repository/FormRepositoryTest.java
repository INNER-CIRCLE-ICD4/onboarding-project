package com.multi.sungwoongonboarding.forms.application.repository;

import com.multi.sungwoongonboarding.common.config.JpaAuditingConfig;
import com.multi.sungwoongonboarding.forms.domain.Forms;
import com.multi.sungwoongonboarding.forms.infrastructure.FormRepositoryImpl;
import com.multi.sungwoongonboarding.options.domain.Options;
import com.multi.sungwoongonboarding.questions.domain.Questions;
import com.multi.sungwoongonboarding.questions.dto.QuestionCreateRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static com.multi.sungwoongonboarding.questions.domain.Questions.QuestionType.*;
import static org.assertj.core.api.Assertions.*;


@Import({FormRepositoryImpl.class})
@ExtendWith(SpringExtension.class)
@DataJpaTest
@EnableJpaAuditing
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
        assertThat(all.size()).isEqualTo(1);
        assertThat(all.get(0).getTitle()).isEqualTo("테스트 폼");

    }


    @Test
    @DisplayName("Form을 저장한다 - 질문 형식 (단문, 장문")
     public void saveFormWithQuestions() {

        // Given
        List<Questions> questionCreateRequests = List.of(
                Questions.builder()
                        .questionText("장문 질문")
                        .questionType(LONG_ANSWER)
                        .build(),
                Questions.builder()
                        .questionText("단문 질문")
                        .questionType(SHORT_ANSWER)
                        .build()
        );

        Forms formsDomain = Forms.builder()
                .title("설문 제목")
                .description("설문 설명")
                .questions(questionCreateRequests)
                .build();

        // When
        Forms save = formRepository.save(formsDomain);
        List<Forms> all = formRepository.findAll();


        // Then
        assertThat(save).isNotNull();
        assertThat(save.getQuestions().size()).isEqualTo(2);
        assertThat(all.size()).isEqualTo(1);
        assertThat(all.get(0).getQuestions().size()).isEqualTo(2);
    }

    @Test
    @DisplayName("Form을 저장한다 - 질문 형식 (단일 선택, 다중 선택)")
    public void saveFormWithQuestionsAndAnswers() {
        // Given


        List<Options> 옵션_항목 = List.of(
                Options.builder().optionText("옵션1").build(),
                Options.builder().optionText("옵션1").build()
        );


        Questions 단일_선택_질문 = Questions.builder()
                .questionText("단일 선택 질문")
                .questionType(SINGLE_CHOICE)
                .options(옵션_항목)
                .build();


        Questions 다중_선택_질문 = Questions.builder()
                .questionText("다중 선택 질문")
                .questionType(MULTIPLE_CHOICE)
                .options(옵션_항목)
                .build();

        Forms formsDomain = Forms.builder()
                .title("설문 제목")
                .description("설문 설명")
                .questions(List.of(단일_선택_질문, 다중_선택_질문))
                .build();

        // When
        Forms save = formRepository.save(formsDomain);
        List<Forms> all = formRepository.findAll();

        // Then
        //설문지 검증
        assertThat(save).isNotNull();
        assertThat(save.getCreatedAt()).isNotNull();
        assertThat(all.size()).isEqualTo(1);

        //질문 검증
        assertThat(save.getQuestions().size()).isEqualTo(2);
        assertThat(all.get(0).getQuestions().size()).isEqualTo(2);

        //옵션 검증
        assertThat(save.getQuestions().get(0).getOptions().size()).isEqualTo(2);
        assertThat(save.getQuestions().get(1).getOptions().size()).isEqualTo(2);
        assertThat(all.get(0).getQuestions().get(0).getOptions().size()).isEqualTo(2);
        assertThat(all.get(0).getQuestions().get(1).getOptions().size()).isEqualTo(2);
    }
}