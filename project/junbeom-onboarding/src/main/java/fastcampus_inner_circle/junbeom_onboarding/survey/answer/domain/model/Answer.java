package fastcampus_inner_circle.junbeom_onboarding.survey.answer.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Builder;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Answer {
    private Long id;
    private Long formId;
    private LocalDateTime submittedAt;
    private List<AnswerDetail> details;
} 