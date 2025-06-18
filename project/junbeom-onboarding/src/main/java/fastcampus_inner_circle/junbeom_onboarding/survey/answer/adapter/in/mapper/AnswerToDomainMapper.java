package fastcampus_inner_circle.junbeom_onboarding.survey.answer.adapter.in.mapper;

import fastcampus_inner_circle.junbeom_onboarding.survey.answer.adapter.in.dto.AnswerRequest;
import fastcampus_inner_circle.junbeom_onboarding.survey.answer.domain.model.Answer;
import fastcampus_inner_circle.junbeom_onboarding.survey.answer.domain.model.AnswerDetail;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
public class AnswerToDomainMapper {
    public static Answer toDomain(AnswerRequest dto) {
        List<AnswerDetail> details = dto.getAnswers()
                .stream()
                .map(detailDto -> AnswerDetail.builder()
                        .contentId(detailDto.getContentId())
                        .optionIds(detailDto.getOptionIds())
                        .answerValue(detailDto.getAnswerValue())
                        .build()
        ).collect(Collectors.toList());
        return Answer.builder()
                .formId(dto.getFormId())
                .submittedAt(LocalDateTime.now())
                .details(details)
                .build();
    }
}
