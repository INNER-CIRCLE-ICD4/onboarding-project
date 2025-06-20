package fastcampus_inner_circle.junbeom_onboarding.survey.answer.adapter.out.mapper;

import fastcampus_inner_circle.junbeom_onboarding.survey.answer.adapter.out.entity.AnswerDetailJpaEntity;
import fastcampus_inner_circle.junbeom_onboarding.survey.answer.adapter.out.entity.AnswerDetailOptionJpaEntity;
import fastcampus_inner_circle.junbeom_onboarding.survey.answer.adapter.out.entity.AnswerJpaEntity;
import fastcampus_inner_circle.junbeom_onboarding.survey.answer.domain.model.Answer;
import fastcampus_inner_circle.junbeom_onboarding.survey.answer.domain.model.AnswerDetail;
import fastcampus_inner_circle.junbeom_onboarding.survey.answer.domain.model.AnswerDetailOption;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class AnswerToDomainMapper {

    public static Answer toDomain(AnswerJpaEntity entity) {
        return Answer.builder()
                .id(entity.getId())
                .formId(entity.getFormId())
                .submittedAt(entity.getSubmittedAt())
                .details(convertDetails(entity.getDetails()))
                .build();
    }

    private static List<AnswerDetail> convertDetails(List<AnswerDetailJpaEntity> details) {
        if (details == null) {
            return Collections.emptyList();
        }
        return details.stream()
                .map(AnswerToDomainMapper::convertDetail)
                .collect(Collectors.toList());
    }

    private static AnswerDetail convertDetail(AnswerDetailJpaEntity entity) {
        return AnswerDetail.builder()
                .id(entity.getId())
                .contentId(entity.getContentId())
                .value(entity.getValue())
                // .questionContent(entity.getQuestionContent()) // 필요시 추가
                .options(convertOptions(entity.getOptions()))
                .build();
    }

    private static List<AnswerDetailOption> convertOptions(List<AnswerDetailOptionJpaEntity> options) {
        if (options == null) {
            return Collections.emptyList();
        }
        return options.stream()
                .map(opt -> AnswerDetailOption.builder()
                        .id(opt.getId())
                        // .contentId(opt.getContentId()) // 필요시 추가
                        // .optionId(opt.getOptionId()) // 필요시 추가
                        .text(opt.getText())
                        .build())
                .collect(Collectors.toList());
    }
}

