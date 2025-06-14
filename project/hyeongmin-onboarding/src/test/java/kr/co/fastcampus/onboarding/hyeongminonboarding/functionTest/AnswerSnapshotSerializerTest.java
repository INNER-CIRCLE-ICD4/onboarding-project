package kr.co.fastcampus.onboarding.hyeongminonboarding.functionTest;


import kr.co.fastcampus.onboarding.hyeongminonboarding.domain.api.survey.util.AnswerSnapshotSerializer;
import kr.co.fastcampus.onboarding.hyeongminonboarding.domain.entity.Question;
import kr.co.fastcampus.onboarding.hyeongminonboarding.domain.entity.QuestionOption;
import kr.co.fastcampus.onboarding.hyeongminonboarding.domain.entity.enums.QuestionType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@DisplayName("AnswerSnapshotSerializer 테스트")
public class AnswerSnapshotSerializerTest {

    @Autowired
    private AnswerSnapshotSerializer serializer;

    @Test
    @DisplayName("질문 + 옵션 스냅샷 직렬화/역직렬화 테스트")
    void test__SnapshotSerialization() {
        // given
        Question question = Question.builder()
                .id(1L)
                .title("당신의 취미는?")
                .type(QuestionType.SINGLE_CHOICE)
                .required(true)
                .build();

        List<QuestionOption> options = List.of(
                QuestionOption.builder().id(10L).optionValue("독서").question(question).build(),
                QuestionOption.builder().id(11L).optionValue("운동").question(question).build()
        );

        // when
        String questionJson = serializer.serializeQuestion(question);
        String optionJson = serializer.serializeOptions(options);

        Question restoredQuestion = serializer.deserializeQuestion(questionJson);
        List<QuestionOption> restoredOptions = serializer.deserializeOptions(optionJson);

        // then
        assertEquals(question.getTitle(), restoredQuestion.getTitle());
        assertEquals(2, restoredOptions.size());
        assertEquals("운동", restoredOptions.get(1).getOptionValue());
    }
}
