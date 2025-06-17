package com.multi.sungwoongonboarding.responses.infrastructure.responses;

import com.multi.sungwoongonboarding.forms.application.repository.FormRepository;
import com.multi.sungwoongonboarding.forms.domain.Forms;
import com.multi.sungwoongonboarding.forms.dto.FormCreateRequest;
import com.multi.sungwoongonboarding.questions.dto.QuestionCreateRequest;
import com.multi.sungwoongonboarding.responses.application.repository.ResponseRepository;
import com.multi.sungwoongonboarding.responses.domain.Responses;
import com.multi.sungwoongonboarding.responses.dto.AnswerCreateRequest;
import com.multi.sungwoongonboarding.responses.dto.ResponseCreateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
class ResponseRepositoryImplTest {

    @Autowired
    FormRepository formRepository;

    @Autowired
    ResponseRepository responseRepository;

    @Autowired
    ResponsesJpaRepository responsesJpaRepository;

    private Forms forms;

    @BeforeEach
    public void setUp() {

        FormCreateRequest form = FormCreateRequest.builder()
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
                                .build()
                ))
                .build();

        forms = formRepository.save(form.toDomain());
    }

    @Test
    @DisplayName("응답을 저장한다. - 성공")
    @Transactional
    public void save_response() {

        //Given
        ResponseCreateRequest 응답지_요청_값 = ResponseCreateRequest.builder()
                .formId(forms.getId())
                .userId("sungwoong")
                .answerCreateRequests(
                        List.of(
                                new AnswerCreateRequest(forms.getQuestions().get(0).getId(), null, "답변 테스트"),
                                new AnswerCreateRequest(forms.getQuestions().get(1).getId(), null, "답변 테스트222")
                        )
                )
                .build();

        //When
        Responses 응답지_저장 = responseRepository.save(응답지_요청_값.toDomain());
        Optional<ResponseJpaEntity> 응답지_조회_1 = responsesJpaRepository.findById(응답지_저장.getId());

        //Then
        assertThat(응답지_조회_1.isPresent()).isTrue();
        assertThat(응답지_조회_1.get().getAnswers()).size().isEqualTo(2);
        assertThat(응답지_조회_1.get().getFormVersion()).isEqualTo(1);
    }

}