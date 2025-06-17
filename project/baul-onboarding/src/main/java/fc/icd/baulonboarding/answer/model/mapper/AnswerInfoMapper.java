package fc.icd.baulonboarding.answer.model.mapper;

import fc.icd.baulonboarding.answer.model.dto.AnswerInfo;
import fc.icd.baulonboarding.answer.model.entity.Answer;
import fc.icd.baulonboarding.answer.model.entity.AnswerItem;
import fc.icd.baulonboarding.answer.model.entity.AnswerItemOption;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR,
        unmappedTargetPolicy = ReportingPolicy.ERROR
)
public interface AnswerInfoMapper {

    @Mapping(source = "survey.id", target = "surveyId")
    @Mapping(source = "answerItemList", target = "answerItemList")
    AnswerInfo.Answer of(Answer answer);

    @Mapping(source = "answer.id", target = "answerId")
    @Mapping(source = "surveyItem.id", target = "surveyItemId")
    AnswerInfo.AnswerItem toAnswerItem(AnswerItem item);

    @Mapping(source = "answerItem.id", target = "answerItemId")
    @Mapping(source = "surveyItemOption.id", target = "optionId")
    AnswerInfo.AnswerItemOption toAnswerItemOption(AnswerItemOption option);

}
