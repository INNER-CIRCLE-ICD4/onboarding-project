package com.multi.sungwoongonboarding.responses.infrastructure.answers;

import com.multi.sungwoongonboarding.forms.application.repository.FormRepository;
import com.multi.sungwoongonboarding.forms.domain.Forms;
import com.multi.sungwoongonboarding.options.domain.Options;
import com.multi.sungwoongonboarding.questions.application.repository.QuestionRepository;
import com.multi.sungwoongonboarding.questions.domain.Questions;
import com.multi.sungwoongonboarding.responses.application.repository.AnswerRepository;
import com.multi.sungwoongonboarding.responses.application.repository.ResponseRepository;
import com.multi.sungwoongonboarding.responses.domain.Answers;
import com.multi.sungwoongonboarding.responses.domain.Responses;
import com.multi.sungwoongonboarding.responses.dto.AnswerCreateRequest;
import com.multi.sungwoongonboarding.responses.dto.ResponseCreateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static com.multi.sungwoongonboarding.questions.domain.Questions.QuestionType.MULTIPLE_CHOICE;
import static com.multi.sungwoongonboarding.questions.domain.Questions.QuestionType.SINGLE_CHOICE;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class AnswerRepositoryImplTest {

    @Autowired
    FormRepository formRepository;

    @Autowired
    QuestionRepository questionRepository;

    @Autowired
    AnswerRepository answerRepository;

    @Autowired
    ResponseRepository responseRepository;

    Responses 더미_응답지;

    Forms 더미_설문지;

    @BeforeEach
    public void setUp() {
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

        Questions 단일_선택_질문 = Questions.builder()
                .questionText("단일 선택 질문")
                .questionType(SINGLE_CHOICE)
                .options(단일_옵션_항목)
                .build();


        Questions 다중_선택_질문 = Questions.builder()
                .questionText("다중 선택 질문")
                .questionType(MULTIPLE_CHOICE)
                .options(다중_옵션_항목)
                .build();

        Forms formsDomain = Forms.builder()
                .title("설문 제목")
                .description("설문 설명")
                .questions(List.of(단일_선택_질문, 다중_선택_질문))
                .build();
        더미_설문지 = formRepository.save(formsDomain);

        ResponseCreateRequest responses = ResponseCreateRequest.builder()
                .formId(더미_설문지.getId())
                .userId("sungwoong")
                .answerCreateRequests(List.of())
                .build();

        더미_응답지 = responseRepository.save(responses.toDomain());
    }

    @Test
    public void testAnswersRepository() {

        // 현재는 단순히 setUp이 잘 작동하는지 확인하는 용도로 사용
        //Given
        List<AnswerCreateRequest> ansReq = List.of(
                AnswerCreateRequest.builder()
                        .questionId(더미_설문지.getQuestions().get(0).getId())
                        .answerText("단일 선택 답변")
                        .optionId(더미_설문지.getQuestions().get(0).getOptions().get(0).getId())
                        .build(),
                AnswerCreateRequest.builder()
                        .questionId(더미_설문지.getQuestions().get(1).getId())
                        .answerText("다중 선택 답변")
                        .optionId(더미_설문지.getQuestions().get(0).getOptions().get(1).getId())
                        .build(),
                AnswerCreateRequest.builder()
                        .questionId(더미_설문지.getQuestions().get(1).getId())
                        .answerText("다중 선택 답변")
                        .optionId(더미_설문지.getQuestions().get(1).getOptions().get(1).getId())
                        .build()
        );


        List<Answers> answers = new ArrayList<>();
        for (AnswerCreateRequest answerRequest : ansReq) {

            Questions originalQuestion = questionRepository.findById(answerRequest.getQuestionId());
            Answers answer = answerRequest.toDomain();
            answers.add(answer);
        }


        // When
        answerRepository.saveAll(answers);
        List<Answers> all = answerRepository.findAll();

        // Then
        assertThat(all.size()).isEqualTo(3);


    }

}