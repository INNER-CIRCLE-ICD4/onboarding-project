package kr.co.fastcampus.onboarding.hyeongminonboarding.domain.api.survey.util;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.fastcampus.onboarding.hyeongminonboarding.domain.entity.Question;
import kr.co.fastcampus.onboarding.hyeongminonboarding.domain.entity.QuestionOption;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AnswerSnapshotSerializer {

    private final ObjectMapper objectMapper;

    public String serializeQuestion(Question question) {
        try {
            return objectMapper.writeValueAsString(question);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("질문 스냅샷 직렬화 실패", e);
        }
    }

    public String serializeOptions(List<QuestionOption> options) {
        try {
            return objectMapper.writeValueAsString(options);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("선택지 스냅샷 직렬화 실패", e);
        }
    }

    public Question deserializeQuestion(String json) {
        try {
            return objectMapper.readValue(json, Question.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("질문 스냅샷 역직렬화 실패", e);
        }
    }

    public List<QuestionOption> deserializeOptions(String json) {
        try {
            return Arrays.asList(objectMapper.readValue(json, QuestionOption[].class));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("옵션 스냅샷 역직렬화 실패", e);
        }
    }

}
