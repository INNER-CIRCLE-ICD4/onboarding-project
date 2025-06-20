package fastcampus_inner_circle.junbeom_onboarding.survey.questionary.domain.model;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SurveyForm {
    private Long id;
    private String name;
    private String describe;
    private LocalDateTime createAt;
    private List<SurveyContent> contents;

    public void updateNameAndDescribe(String name, String describe) {
        this.name = name;
        this.describe = describe;
    }

    public void updateContents(java.util.List<fastcampus_inner_circle.junbeom_onboarding.survey.questionary.adapter.in.dto.UpdateFormRequest.UpdateContentRequest> contentRequests) {
        if (contentRequests == null) {
            this.contents = java.util.Collections.emptyList();
            return;
        }
        java.util.Map<Long, SurveyContent> existingContentMap = new java.util.HashMap<>();
        if (this.contents != null) {
            for (SurveyContent c : this.contents) {
                if (c.getId() != null) existingContentMap.put(c.getId(), c);
            }
        }
        java.util.List<SurveyContent> updatedContents = new java.util.ArrayList<>();
        for (fastcampus_inner_circle.junbeom_onboarding.survey.questionary.adapter.in.dto.UpdateFormRequest.UpdateContentRequest req : contentRequests) {
            java.util.List<SurveyContentOption> updatedOptions = new java.util.ArrayList<>();
            if (req.getOptions() != null) {
                java.util.Map<Long, SurveyContentOption> existingOptionMap = new java.util.HashMap<>();
                SurveyContent existingContent = req.getId() != null ? existingContentMap.get(req.getId()) : null;
                if (existingContent != null && existingContent.getOptions() != null) {
                    for (SurveyContentOption o : existingContent.getOptions()) {
                        if (o.getId() != null) existingOptionMap.put(o.getId(), o);
                    }
                }
                for (fastcampus_inner_circle.junbeom_onboarding.survey.questionary.adapter.in.dto.UpdateFormRequest.UpdateContentRequest.UpdateOptionRequest optReq : req.getOptions()) {
                    if (optReq.getId() == null) {
                        // 신규 옵션
                        updatedOptions.add(SurveyContentOption.builder()
                                .text(optReq.getText())
                                .build());
                    } else if (existingOptionMap.containsKey(optReq.getId())) {
                        // 기존 옵션 수정
                        SurveyContentOption existOpt = existingOptionMap.get(optReq.getId());
                        existOpt.update(optReq.getText());
                        updatedOptions.add(existOpt);
                        existingOptionMap.remove(optReq.getId());
                    }
                    // else: 잘못된 id는 무시
                }
                // existingOptionMap에 남은 값들은 삭제 대상(아무 처리 안 함)
            }
            if (req.getId() == null) {
                // 신규 문항
                updatedContents.add(SurveyContent.builder()
                        .name(req.getName())
                        .describe(req.getDescribe())
                        .type(fastcampus_inner_circle.junbeom_onboarding.survey.questionary.adapter.out.entity.SurveyContentType.valueOf(req.getType()))
                        .isRequired(req.isRequired())
                        .options(updatedOptions)
                        .build());
            } else if (existingContentMap.containsKey(req.getId())) {
                // 기존 문항 수정
                SurveyContent existContent = existingContentMap.get(req.getId());
                existContent.update(
                        req.getName(),
                        req.getDescribe(),
                        fastcampus_inner_circle.junbeom_onboarding.survey.questionary.adapter.out.entity.SurveyContentType.valueOf(req.getType()),
                        req.isRequired(),
                        updatedOptions
                );
                updatedContents.add(existContent);
                existingContentMap.remove(req.getId());
            }
            // else: 잘못된 id는 무시
        }
        // existingContentMap에 남은 값들은 삭제 대상(아무 처리 안 함)
        this.contents = updatedContents;
    }
}
