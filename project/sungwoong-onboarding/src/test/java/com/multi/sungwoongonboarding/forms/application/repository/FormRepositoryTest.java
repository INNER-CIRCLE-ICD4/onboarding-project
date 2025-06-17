package com.multi.sungwoongonboarding.forms.application.repository;

import com.multi.sungwoongonboarding.forms.domain.Forms;
import com.multi.sungwoongonboarding.forms.infrastructure.FormRepositoryImpl;
import com.multi.sungwoongonboarding.options.domain.Options;
import com.multi.sungwoongonboarding.options.infrastructure.OptionsJpaRepository;
import com.multi.sungwoongonboarding.questions.domain.Questions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.multi.sungwoongonboarding.questions.domain.Questions.QuestionType.*;
import static org.assertj.core.api.Assertions.*;


@Import({FormRepositoryImpl.class})
@ExtendWith(SpringExtension.class)
@DataJpaTest
@Transactional
@EnableJpaAuditing
public class FormRepositoryTest {

    @Autowired
    FormRepository formRepository;

    @Autowired
    OptionsJpaRepository optionsJpaRepository;

    @Test
    @DisplayName("Forms 엔티티가 저장되고 조회되는지 테스트")
    public void formSave() {

        // Given
        Forms formsDomain = Forms.builder()
                .title("테스트 폼")
                .description("테스트 폼 설명")
                .version(1)
                .build();

        //When
        Forms savedForms = formRepository.save(formsDomain);
        List<Forms> all = formRepository.findAll();

        //Then
        assertThat(all.size()).isEqualTo(1);
        assertThat(all.get(0).getTitle()).isEqualTo("테스트 폼");
        assertThat(all.get(0).getVersion()).isEqualTo(1);

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
        assertThat(all.get(0).getQuestions().get(0).isDeleted()).isEqualTo(false);

        //옵션 검증
        assertThat(save.getQuestions().get(0).getOptions().size()).isEqualTo(2);
        assertThat(save.getQuestions().get(1).getOptions().size()).isEqualTo(2);
        assertThat(all.get(0).getQuestions().get(0).getOptions().size()).isEqualTo(2);
        assertThat(all.get(0).getQuestions().get(1).getOptions().size()).isEqualTo(2);
    }


    @Test
    @DisplayName("Form을 업데이트한다")
    public void updateForm() {
        // Given
        // 1. 원본 폼 생성 및 저장
        Forms originalForm = Forms.builder()
                .title("원본 설문 제목")
                .description("원본 설문 설명")
                .questions(List.of(
                        Questions.builder()
                                .questionText("원본 질문")
                                .questionType(SHORT_ANSWER)
                                .build()
                ))
                .build();

        Forms savedForm = formRepository.save(originalForm);
        Long formId = savedForm.getId();

        // 2. 업데이트할 폼 데이터 생성
        List<Options> 새_옵션_항목 = List.of(
                Options.builder().optionText("새 옵션1").build(),
                Options.builder().optionText("새 옵션2").build()
        );

        List<Questions> 새_질문_목록 = List.of(
                Questions.builder()
                        .questionText("새 단일 선택 질문")
                        .questionType(SINGLE_CHOICE)
                        .options(새_옵션_항목)
                        .build(),
                Questions.builder()
                        .questionText("새 장문 질문")
                        .questionType(LONG_ANSWER)
                        .build()
        );

        Forms updatedFormData = Forms.builder()
                .id(formId)
                .title("업데이트된 설문 제목")
                .description("업데이트된 설문 설명")
                .questions(새_질문_목록)
                .build();
        updatedFormData.versionUp();

        // When
        Forms result = formRepository.update(formId, updatedFormData);

        // Then
        // 업데이트된 폼 검증
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(formId);
        assertThat(result.getTitle()).isEqualTo("업데이트된 설문 제목");
        assertThat(result.getDescription()).isEqualTo("업데이트된 설문 설명");
        assertThat(result.getVersion()).isGreaterThan(savedForm.getVersion());

        // 질문 검증
        assertThat(result.getQuestions().size()).isEqualTo(3);
        List<Questions> notDeleteQuestions = result.getQuestions().stream().filter(q -> !q.isDeleted()).toList();
        assertThat(notDeleteQuestions.size()).isEqualTo(2);
        assertThat(notDeleteQuestions.get(0).getQuestionText()).isEqualTo("새 단일 선택 질문");
        assertThat(notDeleteQuestions.get(0).getQuestionType()).isEqualTo(SINGLE_CHOICE);
        assertThat(notDeleteQuestions.get(1).getQuestionText()).isEqualTo("새 장문 질문");
        assertThat(notDeleteQuestions.get(1).getQuestionType()).isEqualTo(LONG_ANSWER);

        // 옵션 검증
        assertThat(notDeleteQuestions.get(0).getOptions().size()).isEqualTo(2);
        assertThat(notDeleteQuestions.get(0).getOptions().get(0).getOptionText()).isEqualTo("새 옵션1");
        assertThat(notDeleteQuestions.get(0).getOptions().get(1).getOptionText()).isEqualTo("새 옵션2");

        // DB에서 다시 조회하여 검증
        List<Forms> all = formRepository.findAll();
        assertThat(all.size()).isEqualTo(1);
        Forms updatedFormInDb = all.get(0);
        assertThat(updatedFormInDb.getTitle()).isEqualTo("업데이트된 설문 제목");
        assertThat(updatedFormInDb.getDescription()).isEqualTo("업데이트된 설문 설명");
        assertThat(updatedFormInDb.getQuestions().size()).isEqualTo(3);
        assertThat(updatedFormInDb.getQuestions().stream().filter(q -> !q.isDeleted()).toList().size()).isEqualTo(2);
    }
}