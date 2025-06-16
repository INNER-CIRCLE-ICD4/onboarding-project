package kr.co.fastcampus.onboarding.hyeongminonboarding.domain.api.survey.service.impl;

import kr.co.fastcampus.onboarding.hyeongminonboarding.domain.api.survey.assembler.SurveyResponseDtoAssembler;
import kr.co.fastcampus.onboarding.hyeongminonboarding.domain.api.survey.dto.request.SubmitAnswerRequest;
import kr.co.fastcampus.onboarding.hyeongminonboarding.domain.api.survey.dto.request.SurveyCreateRequest;
import kr.co.fastcampus.onboarding.hyeongminonboarding.domain.api.survey.dto.request.SurveyUpdateRequest;
import kr.co.fastcampus.onboarding.hyeongminonboarding.domain.api.survey.dto.response.SurveyResponseDto;
import kr.co.fastcampus.onboarding.hyeongminonboarding.domain.api.survey.dto.response.SurveyWithAnswersResponseDto;
import kr.co.fastcampus.onboarding.hyeongminonboarding.domain.api.survey.service.ISurveyService;
import kr.co.fastcampus.onboarding.hyeongminonboarding.domain.entity.Survey;
import kr.co.fastcampus.onboarding.hyeongminonboarding.domain.repository.AnswerRepository;
import kr.co.fastcampus.onboarding.hyeongminonboarding.domain.repository.QuestionOptionRepository;
import kr.co.fastcampus.onboarding.hyeongminonboarding.domain.repository.QuestionRepository;
import kr.co.fastcampus.onboarding.hyeongminonboarding.domain.repository.SurveyRepository;
import kr.co.fastcampus.onboarding.hyeongminonboarding.global.dto.accembler.impl.AssemblerFactory;
import kr.co.fastcampus.onboarding.hyeongminonboarding.global.dto.request.BaseRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static kr.co.fastcampus.onboarding.hyeongminonboarding.domain.api.survey.assembler.keys.SurveyContextKey.SURVEY_CONTEXT_KEY;


@Service
@RequiredArgsConstructor
public class SurveyService implements ISurveyService {

    private final AnswerRepository answerRepository;
    private final QuestionOptionRepository questionOptionRepository;
    private final QuestionRepository questionRepository;
    private final SurveyRepository surveyRepository;
    private final SurveyResponseDtoAssembler surveyAssembler;
    private final AssemblerFactory assemblerFactory;


    @Transactional
    @Override
    public SurveyResponseDto createSurvey(BaseRequest<SurveyCreateRequest> request) {
        SurveyCreateRequest req = request.getBody();

        Survey survey = Survey.builder()
                .title(req.getTitle())
                .description(req.getDescription())
                .version(1)
                .build();

        return assemblerFactory.assemble(SurveyResponseDto.class, assemblyContext -> {
            assemblyContext.put(SURVEY_CONTEXT_KEY, survey);
        });
    }

    @Override
    @Transactional
    public SurveyResponseDto updateSurvey(Long surveyId, BaseRequest<SurveyUpdateRequest> request) {
        return null;
    }

    @Override
    @Transactional
    public void submitSurveyAnswer(Long surveyId, BaseRequest<SubmitAnswerRequest> request) {

    }

    @Override
    public SurveyWithAnswersResponseDto getSurveyResponses(Long surveyId) {
        return null;
    }
}
