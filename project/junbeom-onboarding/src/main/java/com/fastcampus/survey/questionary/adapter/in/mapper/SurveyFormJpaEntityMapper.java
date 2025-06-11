package com.fastcampus.survey.questionary.adapter.in.mapper;

import com.fastcampus.survey.questionary.domain.model.SurveyContent;
import com.fastcampus.survey.questionary.domain.model.SurveyContentOption;
import com.fastcampus.survey.questionary.domain.model.SurveyForm;
import com.fastcampus.survey.questionary.adapter.out.entity.SurveyContentJpaEntity;
import com.fastcampus.survey.questionary.adapter.out.entity.SurveyContentOptionJpaEntity;
import com.fastcampus.survey.questionary.adapter.out.entity.SurveyFormJpaEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SurveyFormJpaEntityMapper {

    public SurveyFormJpaEntity toJpaEntity(SurveyForm form) {
        SurveyFormJpaEntity entity = SurveyFormJpaEntity.builder()
                .id(form.getId())
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
                .id(content.getId())
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
                .id(option.getId())
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
                .formId(entity.getForm() != null ? entity.getForm().getId() : null)
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
                .contentId(entity.getContent() != null ? entity.getContent().getId() : null)
                .text(entity.getText())
                .build();
    }
}