package com.multi.sungwoongonboarding.responses.dto;

import com.multi.sungwoongonboarding.forms.application.repository.FormRepository;
import com.multi.sungwoongonboarding.forms.domain.Forms;
import com.multi.sungwoongonboarding.options.domain.Options;
import com.multi.sungwoongonboarding.questions.domain.Questions;
import com.multi.sungwoongonboarding.responses.application.repository.ResponseRepository;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.multi.sungwoongonboarding.questions.domain.Questions.QuestionType.*;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@EnableJpaAuditing
public class ResponseCreateRequestTest {

    @Autowired
    Validator validator;

    @Autowired
    FormRepository formRepository;

    @Autowired
    ResponseRepository responseRepository;

    @BeforeEach
    public void setUp() {
        // 초기화 작업이 필요한 경우 여기에 작성
        // 예: 데이터베이스 초기화, 테스트 데이터 삽입 등
        // 초기화 작업이 필요한 경우 여기에 작성
        // 예: 데이터베이스 초기화, 테스트 데이터 삽입 등
        List<Options> 단일_옵션_항목 = List.of(
                Options.builder().optionText("단일_옵션1").build(),
                Options.builder().optionText("단일_옵션2").build()
        );

        List<Options> 다중_옵션_항목 = List.of(
                Options.builder().optionText("다중_옵션1").build(),
                Options.builder().optionText("다중_옵션2").build()
        );

        Questions 단문_질문 = Questions.builder()
                .questionText("단문 질문")
                .questionType(SHORT_ANSWER)
                .options(단일_옵션_항목)
                .isRequired(true)
                .build();

        Questions 단일_선택_질문 = Questions.builder()
                .questionText("단일 선택 질문")
                .questionType(SINGLE_CHOICE)
                .options(단일_옵션_항목)
                .isRequired(true)
                .build();


        Questions 다중_선택_질문 = Questions.builder()
                .questionText("다중 선택 질문")
                .questionType(MULTIPLE_CHOICE)
                .options(다중_옵션_항목)
                .isRequired(true)
                .build();

        Forms formsDomain = Forms.builder()
                .title("설문 제목")
                .description("설문 설명")
                .questions(List.of(단일_선택_질문, 다중_선택_질문, 단문_질문))
                .build();
        formRepository.save(formsDomain);

        ResponseCreateRequest responses = ResponseCreateRequest.builder()
                .formId(1L)
                .userId("sungwoong")
                .build();

        responseRepository.save(responses.toDomain());
    }

    @Test
    @DisplayName("ResponseCreateRequest 요청 객체 유효성 검사 - 섦문지 pk, 작성자, 응답 내용은 필수이다.")
    public void testResponseCreateRequestValidation() {
        // Given
        ResponseCreateRequest request = new ResponseCreateRequest(null, "", null);


        // When
        var violations = validator.validate(request);


        // Then
        assertThat(violations.size()).isEqualTo(3);
    }


    @Test
    @DisplayName("ResponseCreateRequest 요청 객체 유효성 검사 - 응답 내용은 질문의 유형과 .")
    public void testResponseCreateRequestValidation2() {

        // Given
        ResponseCreateRequest 유효한_응답지_요청값 = new ResponseCreateRequest(
                1L,
                "sungwoong",
                List.of(
                        new AnswerCreateRequest(1L, 1L, null), // 단일 선택 질문에 대한 응답
                        new AnswerCreateRequest(2L, 3L, null), // 다중 선택 질문에 대한 응답
                        new AnswerCreateRequest(2L, 4L, null) // 다중 선택 질문에 대한 응답
                ));

        ResponseCreateRequest 유효하지_않은_요청값_필수질문의응답이빠짐 = new ResponseCreateRequest(1L, "sungwoong", List.of());

        ResponseCreateRequest 유효하지_않은_요청값_선택질문응답이_올바르지않음 = new ResponseCreateRequest(1L, "sungwoong", List.of(
                new AnswerCreateRequest(1L, 3L, null),
                new AnswerCreateRequest(1L, 4L, null),
                new AnswerCreateRequest(1L, 5L, null)
        ));

        // When
        var 요청검증_성공 = validator.validate(유효한_응답지_요청값);
        var 요청검증_실패_질문응답_비어있음 = validator.validate(유효하지_않은_요청값_필수질문의응답이빠짐);
        var 요청검증_실패_선택질문응답이_올바르지않음 = validator.validate(유효하지_않은_요청값_선택질문응답이_올바르지않음);

        // Then
        assertThat(요청검증_성공).isEmpty();
        assertThat(요청검증_실패_질문응답_비어있음.size()).isEqualTo(1);
        assertThat(요청검증_실패_선택질문응답이_올바르지않음.size()).isEqualTo(3);


    }


}