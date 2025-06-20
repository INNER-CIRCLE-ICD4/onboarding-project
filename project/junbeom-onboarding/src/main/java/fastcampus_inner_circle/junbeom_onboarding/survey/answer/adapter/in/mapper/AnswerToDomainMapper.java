package fastcampus_inner_circle.junbeom_onboarding.survey.answer.adapter.in.mapper;

import fastcampus_inner_circle.junbeom_onboarding.survey.answer.adapter.in.dto.AnswerRequest;
import fastcampus_inner_circle.junbeom_onboarding.survey.answer.domain.model.Answer;
import fastcampus_inner_circle.junbeom_onboarding.survey.answer.domain.model.AnswerDetail;
import fastcampus_inner_circle.junbeom_onboarding.survey.answer.domain.model.AnswerDetailOption;
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
        return Answer.builder()
                .formId(dto.getFormId())
                .formName(dto.getFormName())
                .submittedAt(LocalDateTime.now())
                .details(convertDetails(dto.getAnswers()))
                .build();
    }

    private static List<AnswerDetail> convertDetails(List<AnswerRequest.AnswerDetail> answerDetails) {
        return answerDetails.stream()
                .map(AnswerToDomainMapper::convertDetail)
                .collect(Collectors.toList());
    }

    private static AnswerDetail convertDetail(AnswerRequest.AnswerDetail dto) {
        return AnswerDetail.builder()
                .contentId(dto.getContentId())
                .contentName(dto.getContentName())
                .contentDescribe(dto.getContentDescribe())
                .type(dto.getType())
                .value(dto.getValue())
                .options(convertOptions(dto.getOptions()))
                .build();
    }


    private static List<AnswerDetailOption> convertOptions(List<AnswerRequest.AnswerDetailOptions> details) {

        return details.stream()
                .map(opt -> AnswerDetailOption.builder()
                        .optionId(opt.getOptionId())
                        .text(opt.getText())
                        .build())
                .collect(Collectors.toList());
    }
}
