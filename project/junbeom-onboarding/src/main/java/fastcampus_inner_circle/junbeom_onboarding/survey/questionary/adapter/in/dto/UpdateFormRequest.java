package fastcampus_inner_circle.junbeom_onboarding.survey.questionary.adapter.in.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateFormRequest {
    private String name;
    private String describe;
    @Size(min = 1, max = 10, message = "설문 항목은 1개 이상 10개 이하로 입력해야 합니다.")
    private List<UpdateContentRequest> contents;

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UpdateContentRequest {
        private Long id; // 기존 항목 수정/삭제 시 필요, 신규 추가 시 null
        private String name;
        private String describe;
        private String type;
        @JsonProperty("isRequired")
        private boolean isRequired;
        private List<UpdateOptionRequest> options;

        @Getter
        @NoArgsConstructor
        @AllArgsConstructor
        @Builder
        public static class UpdateOptionRequest {
            private Long id; // 기존 옵션 수정/삭제 시 필요, 신규 추가 시 null
            private String text;
        }
    }
} 