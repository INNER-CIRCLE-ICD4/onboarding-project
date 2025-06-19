package com.multi.sungwoongonboarding.forms.dto;

import com.multi.sungwoongonboarding.forms.domain.Forms;
import com.multi.sungwoongonboarding.questions.dto.QuestionCreateRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Getter
@NoArgsConstructor
public class FormCreateRequest {

    @NotBlank(message = "제목은 필수 입력 항목입니다.")
    private String title;

    private String description;

    @Valid
    @NotNull(message = "질문 목록은 필수 입력 항목입니다.")
    @Size(min = 1, max = 10, message = "질문은 최소 1개에서 최대 10개까지 입력할 수 있습니다.")
    //todo 질문 수정 요청 값은 삭제하려는 질문 목록도 있으므로 삭제 요청을 제외한 질문이 10개 이하인지 체크 해줘야 한다.
    private List<QuestionCreateRequest> questionCreateRequests;


    @Builder
    public FormCreateRequest(String title, String description, List<QuestionCreateRequest> questionCreateRequests) {
        this.title = title;
        this.description = description;
        this.questionCreateRequests = questionCreateRequests;
    }

    public Forms toDomain() {
        Forms.FormsBuilder formBuilder = Forms.builder()
                .title(this.title)
                .version(1)
                .description(this.description);

        if (this.questionCreateRequests != null) {
            formBuilder.questions(this.questionCreateRequests.stream().map(QuestionCreateRequest::toDomain).toList());
        }

        return formBuilder.build();
    }

}
