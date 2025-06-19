package fc.icd.baulonboarding.survey.model.mapper;

import fc.icd.baulonboarding.survey.model.dto.SurveyCommand;
import fc.icd.baulonboarding.survey.model.dto.SurveyDto;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface SurveyDtoMapper {
    SurveyCommand.RegisterSurvey of(SurveyDto.RegisterSurveyRequest request);

    SurveyCommand.UpdateSurvey of(SurveyDto.UpdateSurveyRequest request);

}
