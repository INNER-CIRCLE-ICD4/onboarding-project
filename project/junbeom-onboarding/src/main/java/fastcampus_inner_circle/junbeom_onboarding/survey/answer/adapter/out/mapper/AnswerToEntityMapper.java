package fastcampus_inner_circle.junbeom_onboarding.survey.answer.adapter.out.mapper;

import fastcampus_inner_circle.junbeom_onboarding.survey.answer.adapter.out.entity.AnswerDetailJpaEntity;
import fastcampus_inner_circle.junbeom_onboarding.survey.answer.adapter.out.entity.AnswerDetailOptionJpaEntity;
import fastcampus_inner_circle.junbeom_onboarding.survey.answer.adapter.out.entity.AnswerJpaEntity;
import fastcampus_inner_circle.junbeom_onboarding.survey.answer.domain.model.Answer;
import fastcampus_inner_circle.junbeom_onboarding.survey.answer.domain.model.AnswerDetail;
import fastcampus_inner_circle.junbeom_onboarding.survey.questionary.adapter.out.repository.SurveyContentOptionJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class AnswerToEntityMapper {
    private final SurveyContentOptionJpaRepository optionRepository;

    public AnswerJpaEntity toJpaEntity(Answer answer) {
        AnswerJpaEntity entity = AnswerJpaEntity.builder()
                .formId(answer.getFormId())
                .submittedAt(answer.getSubmittedAt())
                .build();

        if (answer.getDetails() != null) {
            List<AnswerDetailJpaEntity> details = answer.getDetails().stream()
                    .map(detail -> toJpaEntity(detail, entity))
                    .collect(Collectors.toList());
            entity.setDetails(details);
        }
        return entity;
    }

    public AnswerDetailJpaEntity toJpaEntity(AnswerDetail detail, AnswerJpaEntity answerEntity) {
        AnswerDetailJpaEntity entity = AnswerDetailJpaEntity.builder()
                .contentId(detail.getContentId())
                .answerValue(detail.getAnswerValue())
                .questionContent(detail.getQuestionContent())
                .answer(answerEntity)
                .build();

        if (detail.getOptionIds() != null) {
            List<AnswerDetailOptionJpaEntity> options = detail.getOptionIds().stream()
                    .map(optionId -> {
                        String text = optionRepository.findById(optionId)
                                .map(opt -> opt.getText())
                                .orElse(null);
                        return AnswerDetailOptionJpaEntity.builder()
                                .answerDetail(entity)
                                .text(text)
                                .build();
                    })
                    .collect(Collectors.toList());
            entity.setOptions(options);
        }
        return entity;
    }




/*    @Component
    @RequiredArgsConstructor
    public class AnswerToEntityMapper {
        private final SurveyContentOptionJpaRepository optionRepository;

        public AnswerJpaEntity toJpaEntity(Answer answer) {
            AnswerJpaEntity entity = AnswerJpaEntity.builder()
                    .formId(answer.getFormId())
                    .submittedAt(answer.getSubmittedAt())
                    .build();

            if (answer.getDetails() != null) {
                List<AnswerDetailJpaEntity> details = answer.getDetails().stream()
                        .map(detail -> toJpaEntity(detail, entity))
                        .collect(Collectors.toList());
                entity.setDetails(details);
            }
            return entity;
        }

        public AnswerDetailJpaEntity toJpaEntity(AnswerDetail detail, AnswerJpaEntity answerEntity) {
            AnswerDetailJpaEntity entity = AnswerDetailJpaEntity.builder()
                    .contentId(detail.getContentId())
                    .answerValue(detail.getAnswerValue())
                    .questionContent(detail.getQuestionContent())
                    .answer(answerEntity)
                    .build();

            if (detail.getOptionIds() != null && !detail.getOptionIds().isEmpty()) {
                List<AnswerDetailOptionJpaEntity> options = detail.getOptionIds().stream()
                        .map(optionId -> {
                            // 옵션명을 옵션 테이블에서 조회
                            String text = optionRepository.findById(optionId)
                                    .map(opt -> opt.getText())
                                    .orElse(null);
                            return AnswerDetailOptionJpaEntity.builder()
                                    .answerDetail(entity)
                                    .optionId(optionId) // 옵션ID도 함께 저장
                                    .text(text)
                                    .build();
                        })
                        .collect(Collectors.toList());
                entity.setOptions(options);
            }
            return entity;
        }
    }*/

}
