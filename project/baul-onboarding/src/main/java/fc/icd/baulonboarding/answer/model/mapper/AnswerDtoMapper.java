package fc.icd.baulonboarding.answer.model.mapper;

import fc.icd.baulonboarding.answer.model.dto.AnswerCommand;
import fc.icd.baulonboarding.answer.model.dto.AnswerDto;
import fc.icd.baulonboarding.answer.model.dto.AnswerInfo;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface AnswerDtoMapper {

    AnswerCommand.RegisterAnswer of(AnswerDto.RegisterAnswerRequest request);

    AnswerDto.Answer of(AnswerInfo.Answer answerResult);

}
