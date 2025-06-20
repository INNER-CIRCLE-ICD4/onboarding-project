package fastcampus_inner_circle.junbeom_onboarding.survey.answer.domain.model;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Answer {
    private Long id;
    private Long formId;
    private String formName;
    private LocalDateTime submittedAt;
    private List<AnswerDetail> details;
} 