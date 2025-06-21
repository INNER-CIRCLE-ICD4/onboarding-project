package survey.survey.controller.request.survey.update;

import jakarta.validation.constraints.NotBlank;

public record CandidateUpdateRequest(int checkCandidateIndex,
                                     @NotBlank(message = "선택 항목 값은 필수입니다") String name) {
    public CandidateUpdateRequest(int checkCandidateIndex, String name) {
        this.checkCandidateIndex = checkCandidateIndex;
        this.name = name;
    }
}
