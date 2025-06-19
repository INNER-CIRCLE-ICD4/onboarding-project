package com.multi.sungwoongonboarding.forms.application;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.multi.sungwoongonboarding.forms.application.repository.FormRepository;
import com.multi.sungwoongonboarding.forms.domain.Forms;
import com.multi.sungwoongonboarding.forms.dto.FormCreateRequest;
import com.multi.sungwoongonboarding.forms.dto.FormResponse;
import com.multi.sungwoongonboarding.options.dto.OptionCreateRequest;
import com.multi.sungwoongonboarding.questions.dto.QuestionCreateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@EnableJpaAuditing
public class FormServiceTest {

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    FormService formService;

    @Autowired
    FormRepository formRepository;

    @Test
    @DisplayName("설문 조사서 등록 테스트")
    public void testCreateForm() throws JsonProcessingException {
        // Given
        // 설문 조사서 등록에 필요한 데이터 준비
        FormCreateRequest formCreateRequest = createAllQuestionTypesFormRequest();

        // When
        // FormService를 사용하여 설문 조사서 등록
        FormResponse form = formService.createForms(formCreateRequest);
        List<Forms> all = formRepository.findAll();

        // Then
        // 등록된 설문 조사서가 올바르게 저장되었는지 검증
        assertThat(form).isNotNull();
        assertThat(all.size()).isEqualTo(1);
        assertThat(all.get(0).getQuestions().size()).isEqualTo(4);
    }

    private FormCreateRequest createAllQuestionTypesFormRequest() {
        return FormCreateRequest.builder()
                .title("모든 질문 유형 테스트")
                .description("모든 질문 유형을 테스트합니다.")
                .questionCreateRequests(List.of(
                        QuestionCreateRequest.builder()
                                .questionText("단문형 질문")
                                .questionType("SHORT_ANSWER")
                                .isRequired(true)
                                .optionCreateRequests(List.of())
                                .build(),
                        QuestionCreateRequest.builder()
                                .questionText("장문형 질문")
                                .questionType("LONG_ANSWER")
                                .isRequired(false)
                                .optionCreateRequests(List.of())
                                .build(),
                        QuestionCreateRequest.builder()
                                .questionText("단일 선택 질문")
                                .questionType("SINGLE_CHOICE")
                                .isRequired(true)
                                .optionCreateRequests(List.of(
                                        new OptionCreateRequest("옵션 1"),
                                        new OptionCreateRequest("옵션 2")
                                ))
                                .build(),
                        QuestionCreateRequest.builder()
                                .questionText("복수 선택 질문")
                                .questionType("MULTIPLE_CHOICE")
                                .isRequired(false)
                                .optionCreateRequests(List.of(
                                        new OptionCreateRequest("옵션 1"),
                                        new OptionCreateRequest("옵션 2")
                                ))
                                .build()
                ))
                .build();
    }
}