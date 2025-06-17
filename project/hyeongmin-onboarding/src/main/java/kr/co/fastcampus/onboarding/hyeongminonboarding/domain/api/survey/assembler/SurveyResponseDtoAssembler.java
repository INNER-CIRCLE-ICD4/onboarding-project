package kr.co.fastcampus.onboarding.hyeongminonboarding.domain.api.survey.assembler;

import kr.co.fastcampus.onboarding.hyeongminonboarding.domain.api.survey.dto.response.SurveyResponseDto;
import kr.co.fastcampus.onboarding.hyeongminonboarding.domain.entity.Question;
import kr.co.fastcampus.onboarding.hyeongminonboarding.domain.entity.QuestionOption;
import kr.co.fastcampus.onboarding.hyeongminonboarding.domain.entity.Survey;
import kr.co.fastcampus.onboarding.hyeongminonboarding.domain.entity.enums.QuestionType;
import kr.co.fastcampus.onboarding.hyeongminonboarding.global.dto.accembler.Assembler;
import kr.co.fastcampus.onboarding.hyeongminonboarding.global.dto.accembler.impl.AssemblyContext;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static kr.co.fastcampus.onboarding.hyeongminonboarding.domain.api.survey.assembler.keys.SurveyContextKey.*;

@Component
public class SurveyResponseDtoAssembler implements Assembler<SurveyResponseDto> {

    @Override
    public SurveyResponseDto assemble(AssemblyContext context) {
        Survey survey = context.get(SURVEY_CONTEXT_KEY);
        List<Question> questions = context.get(QUESTION_LIST_CONTEXT_KEY);
        List<QuestionOption> questionOptions = context.get(QUESTION_OPTION_LIST_CONTEXT_KEY); // TODO : questionOptions 는 리스트가 아니라 Map 형태로 넣어야 할듯
        List<SurveyResponseDto.QuestionResponseDto> questionResponseDtos =
                questions.stream().map(question -> {

                    SurveyResponseDto.QuestionResponseDto questionResponseDto
                            = SurveyResponseDto.QuestionResponseDto.builder()
                            .id(question.getId())
                            .title(question.getTitle())
                            .detail(question.getDetail())
                            .type(question.getType())
                            .required(question.isRequired())
                            .build();

                    if(question.getType() == QuestionType.MULTIPLE_CHOICE ||
                    question.getType() == QuestionType.SINGLE_CHOICE){
                        questionResponseDto.setOptions(
                                questionOptions.stream().filter(f->
                                                Objects.equals(f.getQuestion().getId(), question.getId()))
                                        .map(questionOption ->
                                                SurveyResponseDto.QuestionOptionResponseDto
                                                        .builder()
                                                        .value(questionOption.getOptionValue())
                                                        .id(questionOption.getId())
                                                        .build()
                                        ).collect(Collectors.toList())
                        );
                    }

                    return questionResponseDto;
                }).toList();

        return SurveyResponseDto.builder()
                .surveyId(survey.getId())
                .title(survey.getTitle())
                .description(survey.getDescription())
                .questions(questionResponseDtos)
                .build();
    }

    @Override
    public Class<SurveyResponseDto> supportType() {
        return SurveyResponseDto.class;
    }
}
