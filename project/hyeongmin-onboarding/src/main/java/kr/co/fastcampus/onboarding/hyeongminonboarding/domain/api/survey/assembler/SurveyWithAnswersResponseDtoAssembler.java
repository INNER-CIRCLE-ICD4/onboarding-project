package kr.co.fastcampus.onboarding.hyeongminonboarding.domain.api.survey.assembler;

import kr.co.fastcampus.onboarding.hyeongminonboarding.domain.api.survey.dto.response.SurveyWithAnswersResponseDto;
import kr.co.fastcampus.onboarding.hyeongminonboarding.domain.api.survey.util.AnswerSnapshotSerializer;
import kr.co.fastcampus.onboarding.hyeongminonboarding.domain.entity.*;
import kr.co.fastcampus.onboarding.hyeongminonboarding.global.dto.accembler.Assembler;
import kr.co.fastcampus.onboarding.hyeongminonboarding.global.dto.accembler.impl.AssemblyContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static kr.co.fastcampus.onboarding.hyeongminonboarding.domain.api.survey.assembler.keys.SurveyContextKey.*;


@Component
@RequiredArgsConstructor
public class SurveyWithAnswersResponseDtoAssembler implements Assembler<SurveyWithAnswersResponseDto> {
    private final AnswerSnapshotSerializer serializer;
    @Override
    public SurveyWithAnswersResponseDto assemble(AssemblyContext context) {
        Survey survey = context.get(SURVEY_CONTEXT_KEY);
        List<SurveyResponse> surveyResponses = context.get(SURVEY_RESPONSE_LIST_CONTEXT_KEY);
        List<Answer> answers = context.get(ANSWER_LIST_CONTEXT_KEY);

        List<SurveyWithAnswersResponseDto.SurveyAnswerResponseDto> responses =
                surveyResponses.stream().map(surveyResponse -> {
                   return SurveyWithAnswersResponseDto.SurveyAnswerResponseDto.builder()
                            .submittedAt(surveyResponse.getSubmittedAt())
                            .surveyVersion(surveyResponse.getSurveyVersion())
                            .respondentId(surveyResponse.getId())
                            .answers(answers.stream().filter(
                                    f-> Objects.equals(f.getResponse().getId(), surveyResponse.getId()))
                                    .map(answer -> {
                                        Question question = serializer.deserializeQuestion(answer.getQuestionSnapshotJson());
                                        List<QuestionOption> options = serializer.deserializeOptions(answer.getOptionSnapshotJson());
                                        return SurveyWithAnswersResponseDto.AnswerDetailDto.builder()
                                                .answerText(answer.getAnswerText())
                                                .selectedOptionIds(answer.getSelectedOptionIds())
                                                .questionId(answer.getQuestion().getId())
                                                .required(question.isRequired())
                                                .questionType(question.getType())
                                                .questionTitle(question.getTitle())
                                                .questionDetail(question.getDetail())
                                                .questionSnapshot(SurveyWithAnswersResponseDto.QuestionSnapshotDto.builder()
                                                        .type(question.getType())
                                                        .detail(question.getDetail())
                                                        .title(question.getTitle())
                                                        .required(question.isRequired())
                                                        .build())
                                                .optionSnapshot(options.stream().map(optionSnaps->{
                                                    return SurveyWithAnswersResponseDto.QuestionOptionSnapshotDto.builder()
                                                            .id(optionSnaps.getId())
                                                            .optionValue(optionSnaps.getOptionValue())
                                                            .build();
                                                }).collect(Collectors.toList()))

                                                .build();

                            }).collect(Collectors.toList()))
                            .build();
                }).toList();

        return SurveyWithAnswersResponseDto.builder()
                .surveyId(survey.getId())
                .title(survey.getTitle())
                .description(survey.getDescription())
                .responses(responses)
                .build();
    }

    @Override
    public Class<SurveyWithAnswersResponseDto> supportType() {
        return SurveyWithAnswersResponseDto.class;
    }
}
