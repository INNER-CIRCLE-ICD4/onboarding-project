package fastcampus_inner_circle.junbeom_onboarding.survey.answer.adapter.in.mapper;

import fastcampus_inner_circle.junbeom_onboarding.survey.answer.adapter.in.dto.AnswerResponse;
import fastcampus_inner_circle.junbeom_onboarding.survey.answer.domain.model.Answer;
import fastcampus_inner_circle.junbeom_onboarding.survey.answer.domain.model.AnswerDetail;
import fastcampus_inner_circle.junbeom_onboarding.survey.answer.domain.model.AnswerDetailOption;

import java.util.List;
import java.util.stream.Collectors;

public class AnwerToResponseDtoMapperForUpdate {

    public static AnswerResponse toResponse(Answer domain) {
        return AnswerResponse.builder()
                .answerId(domain.getId())
                .formId(domain.getFormId())
                .formName(domain.getFormName())
                .answers(domain.getDetails() == null ? null :
                        domain.getDetails().stream()
                                .map(AnwerToResponseDtoMapperForUpdate::toDetail)
                                .collect(Collectors.toList()))
                .build();
    }

    private static AnswerResponse.AnswerDetail toDetail(AnswerDetail detail) {
        return AnswerResponse.AnswerDetail.builder()
                .contentId(detail.getContentId())
                .contentName(detail.getContentName())
                .contentDescribe(detail.getContentDescribe())
                .type(detail.getType())
                .value(detail.getValue())
                .options(detail.getOptions() == null ? null :
                        detail.getOptions().stream()
                                .map(AnwerToResponseDtoMapperForUpdate::toOption)
                                .collect(Collectors.toList()))
                .build();
    }

    private static AnswerResponse.AnswerOption toOption(AnswerDetailOption option) {
        return AnswerResponse.AnswerOption.builder()
                .optionId(option.getOptionId())
                .optionName(option.getText())
                .build();
    }
}
