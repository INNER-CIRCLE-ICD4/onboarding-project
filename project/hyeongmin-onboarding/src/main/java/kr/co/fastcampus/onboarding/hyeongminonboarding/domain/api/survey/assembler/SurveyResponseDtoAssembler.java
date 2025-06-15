package kr.co.fastcampus.onboarding.hyeongminonboarding.domain.api.survey.assembler;

import kr.co.fastcampus.onboarding.hyeongminonboarding.domain.api.survey.dto.response.SurveyResponseDto;
import kr.co.fastcampus.onboarding.hyeongminonboarding.domain.entity.Survey;
import kr.co.fastcampus.onboarding.hyeongminonboarding.global.dto.accembler.Assembler;
import kr.co.fastcampus.onboarding.hyeongminonboarding.global.dto.accembler.impl.AssemblyContext;
import org.springframework.stereotype.Component;

import static kr.co.fastcampus.onboarding.hyeongminonboarding.domain.api.survey.assembler.keys.SurveyContextKey.SURVEY_CONTEXT_KEY;

@Component
public class SurveyResponseDtoAssembler implements Assembler<SurveyResponseDto> {

    @Override
    public SurveyResponseDto assemble(AssemblyContext context) {
        Survey survey = context.get(SURVEY_CONTEXT_KEY);

        return SurveyResponseDto.builder()
                .surveyId(survey.getId())
                .title(survey.getTitle())
                .description(survey.getDescription())
                .build();
    }

    @Override
    public Class<SurveyResponseDto> supportType() {
        return SurveyResponseDto.class;
    }
}
