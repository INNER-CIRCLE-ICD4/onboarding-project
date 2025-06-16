package com.multi.sungwoongonboarding.responses.dto;

import com.multi.sungwoongonboarding.responses.domain.Responses;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class ResponseCreateRequest {

    @NotNull(message = "설문지 ID는 필수 입력 항목입니다.")
    private Long formId;

    @NotBlank(message = "사용자 ID는 필수 입력 항목입니다.")
    private String userId;

    @Valid
    @NotEmpty(message = "응답 목록은 필수 입력 항목입니다.")
    //todo 단일 선택에 대한 질문일 경우 답변이 한개만 와야 한다.
    private List<AnswerCreateRequest> answerCreateRequests;

    @Builder
    public ResponseCreateRequest(Long formId, String userId, List<AnswerCreateRequest> answerCreateRequests) {
        this.formId = formId;
        this.userId = userId;
        this.answerCreateRequests = answerCreateRequests;
    }

    public Responses toDomain() {
        Responses.ResponsesBuilder responsesBuilder = Responses.builder()
                .formId(this.formId)
                .userId(this.userId)
                .createdAt(LocalDateTime.now());

        if (this.answerCreateRequests != null) {
            responsesBuilder.answers(
                    this.answerCreateRequests.stream().map(AnswerCreateRequest::toDomain).toList());
        }

        return responsesBuilder
                .build();
    }
}