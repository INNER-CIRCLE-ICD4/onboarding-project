package kr.co.fastcampus.onboarding.hyeongminonboarding.domain.api.survey.util;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.co.fastcampus.onboarding.hyeongminonboarding.domain.api.survey.dto.response.SurveyWithAnswersResponseDto;
import kr.co.fastcampus.onboarding.hyeongminonboarding.domain.entity.Question;
import kr.co.fastcampus.onboarding.hyeongminonboarding.domain.entity.QuestionOption;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class AnswerSnapshotSerializer {

    private final ObjectMapper objectMapper;

    public AnswerSnapshotSerializer(ObjectMapper objectMapper){
        this.objectMapper = objectMapper;
    }

    public String serializeQuestion(Question question) {
        SurveyWithAnswersResponseDto.QuestionSnapshotDto snap = SurveyWithAnswersResponseDto.QuestionSnapshotDto.builder()
                .title(question.getTitle())
                .detail(question.getDetail())
                .type(question.getType())
                .required(question.isRequired())
                .build();
        try {
            return objectMapper.writeValueAsString(snap);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("질문 스냅샷 직렬화 실패", e);
        }
    }

    public String serializeOptions(List<QuestionOption> options) {
        List<SurveyWithAnswersResponseDto.QuestionOptionSnapshotDto> snaps = options.stream()
                .map(opt -> SurveyWithAnswersResponseDto.QuestionOptionSnapshotDto.builder()
                        .id(opt.getId())
                        .optionValue(opt.getOptionValue())
                        .build())
                .toList();
        try {
            return objectMapper.writeValueAsString(snaps);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("선택지 스냅샷 직렬화 실패", e);
        }
    }

    public SurveyWithAnswersResponseDto.QuestionSnapshotDto deserializeQuestion(String json) {
        try {
            return objectMapper.readValue(json, SurveyWithAnswersResponseDto.QuestionSnapshotDto.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("질문 스냅샷 역직렬화 실패", e);
        }
    }

    public List<SurveyWithAnswersResponseDto.QuestionOptionSnapshotDto> deserializeOptions(String json) {
        try {
            return Arrays.asList(objectMapper.readValue(json, SurveyWithAnswersResponseDto.QuestionOptionSnapshotDto[].class));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("옵션 스냅샷 역직렬화 실패", e);
        }
    }

}
