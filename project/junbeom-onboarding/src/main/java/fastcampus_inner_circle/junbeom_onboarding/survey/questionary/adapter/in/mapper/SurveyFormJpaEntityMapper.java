package fastcampus_inner_circle.junbeom_onboarding.survey.questionary.adapter.in.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import fastcampus_inner_circle.junbeom_onboarding.survey.questionary.adapter.out.entity.SurveyContentJpaEntity;
import fastcampus_inner_circle.junbeom_onboarding.survey.questionary.adapter.out.entity.SurveyContentOptionJpaEntity;
import fastcampus_inner_circle.junbeom_onboarding.survey.questionary.adapter.out.entity.SurveyFormJpaEntity;
import fastcampus_inner_circle.junbeom_onboarding.survey.questionary.domain.model.SurveyContent;
import fastcampus_inner_circle.junbeom_onboarding.survey.questionary.domain.model.SurveyContentOption;
import fastcampus_inner_circle.junbeom_onboarding.survey.questionary.domain.model.SurveyForm;

@Component
public class SurveyFormJpaEntityMapper {

    public SurveyFormJpaEntity toJpaEntity(SurveyForm form) {
        SurveyFormJpaEntity entity = SurveyFormJpaEntity.builder()
                .name(form.getName())
                .describe(form.getDescribe())
                .createAt(form.getCreateAt())
                .build();

        if (form.getContents() != null) {
            List<SurveyContentJpaEntity> contentEntities = form.getContents().stream()
                    .map(content -> toJpaEntity(content, entity))
                    .collect(Collectors.toList());
            entity.setContents(contentEntities);
        }

        return entity;
    }

    public SurveyContentJpaEntity toJpaEntity(SurveyContent content, SurveyFormJpaEntity form) {
        SurveyContentJpaEntity entity = SurveyContentJpaEntity.builder()
                .name(content.getName())
                .describe(content.getDescribe())
                .type(content.getType())
                .isRequired(content.isRequired())
                .build();

        entity.setForm(form);
        if (content.getOptions() != null) {
            List<SurveyContentOptionJpaEntity> optionEntities = content.getOptions().stream()
                    .map(option -> toJpaEntity(option, entity))
                    .collect(Collectors.toList());
            entity.setOptions(optionEntities);
        }

        return entity;
    }

    public SurveyContentOptionJpaEntity toJpaEntity(SurveyContentOption option, SurveyContentJpaEntity content) {
        SurveyContentOptionJpaEntity entity = SurveyContentOptionJpaEntity.builder()
                .text(option.getText())
                .build();

        entity.setContent(content);
        return entity;
    }

    public SurveyForm toDomain(SurveyFormJpaEntity entity) {
        return SurveyForm.builder()
                .id(entity.getId())
                .name(entity.getName())
                .describe(entity.getDescribe())
                .createAt(entity.getCreateAt())
                .contents(entity.getContents() != null ? entity.getContents().stream().map(this::toDomain).collect(Collectors.toList()) : null)
                .build();
    }

    public SurveyContent toDomain(SurveyContentJpaEntity entity) {
        return SurveyContent.builder()
                .id(entity.getId())
                .name(entity.getName())
                .describe(entity.getDescribe())
                .type(entity.getType())
                .isRequired(entity.isRequired())
                .options(entity.getOptions() != null ? entity.getOptions().stream().map(this::toDomain).collect(Collectors.toList()) : null)
                .build();
    }

    public SurveyContentOption toDomain(SurveyContentOptionJpaEntity entity) {
        return SurveyContentOption.builder()
                .id(entity.getId())
                .text(entity.getText())
                .build();
    }
}