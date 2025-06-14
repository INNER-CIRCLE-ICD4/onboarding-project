package com.multi.sungwoongonboarding.responses.dto;

import com.multi.sungwoongonboarding.responses.domain.Responses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class ResponseCreateRequest {

    @NotNull(message = "설문지 ID는 필수 입력 항목입니다.")
    private Long formId;

    @NotNull(message = "사용자 ID는 필수 입력 항목입니다.")
    private String userId;

    @Valid
    @NotEmpty(message = "답변 목록은 필수 입력 항목입니다.")
    private List<AnswerCreateRequest> answers;

    @Builder
    public ResponseCreateRequest(Long formId, String userId, List<AnswerCreateRequest> answers) {
        this.formId = formId;
        this.userId = userId;
        this.answers = answers;
    }

    public Responses toDomain() {
        Responses.ResponsesBuilder responsesBuilder = Responses.builder()
                .formId(this.formId)
                .userId(this.userId)
                .createdAt(LocalDateTime.now());

        if (this.answers != null && !this.answers.isEmpty()) {
            responsesBuilder.answers(
                    this.answers.stream().map(AnswerCreateRequest::toDomain).toList());
        }

        return responsesBuilder
                .build();
    }
}