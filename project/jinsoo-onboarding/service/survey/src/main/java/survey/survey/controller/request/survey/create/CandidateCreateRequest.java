package survey.survey.controller.request.survey.create;

import jakarta.validation.constraints.NotBlank;

public record CandidateCreateRequest(
        int checkCandidateIndex,
        @NotBlank(message = "선택 항목 값은 필수입니다") String name) {
    public CandidateCreateRequest(int checkCandidateIndex, String name) {
        this.checkCandidateIndex = checkCandidateIndex;
        this.name = name;
    }
}
