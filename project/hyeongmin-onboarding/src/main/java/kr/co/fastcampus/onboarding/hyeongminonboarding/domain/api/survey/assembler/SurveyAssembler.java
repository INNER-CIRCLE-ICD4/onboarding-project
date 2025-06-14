package kr.co.fastcampus.onboarding.hyeongminonboarding.domain.api.survey.assembler;

import kr.co.fastcampus.onboarding.hyeongminonboarding.domain.api.survey.dto.response.SurveyResponseDto;
import kr.co.fastcampus.onboarding.hyeongminonboarding.domain.entity.Survey;
import kr.co.fastcampus.onboarding.hyeongminonboarding.global.dto.accembler.Assembler;

public class SurveyAssembler implements Assembler<Survey, SurveyResponseDto> {


    @Override
    public SurveyResponseDto toDto(Survey entity) {
        return null;
    }

    @Override
    public Survey toEntity(SurveyResponseDto dto) {
        return null;
    }
}
