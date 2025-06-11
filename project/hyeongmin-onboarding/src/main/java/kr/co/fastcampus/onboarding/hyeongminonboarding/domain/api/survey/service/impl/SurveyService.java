package kr.co.fastcampus.onboarding.hyeongminonboarding.domain.api.survey.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.querydsl.core.util.CollectionUtils;
import kr.co.fastcampus.onboarding.hyeongminonboarding.domain.api.survey.dto.request.SurveyCreateRequest;
import kr.co.fastcampus.onboarding.hyeongminonboarding.domain.api.survey.dto.response.SurveyCreateResponse;
import kr.co.fastcampus.onboarding.hyeongminonboarding.domain.api.survey.service.ISurveyService;
import kr.co.fastcampus.onboarding.hyeongminonboarding.domain.entity.Question;
import kr.co.fastcampus.onboarding.hyeongminonboarding.domain.entity.QuestionOption;
import kr.co.fastcampus.onboarding.hyeongminonboarding.domain.entity.Survey;
import kr.co.fastcampus.onboarding.hyeongminonboarding.domain.entity.enums.QuestionType;
import kr.co.fastcampus.onboarding.hyeongminonboarding.domain.exception.SurveyException;
import kr.co.fastcampus.onboarding.hyeongminonboarding.domain.repository.AnswerRepository;
import kr.co.fastcampus.onboarding.hyeongminonboarding.domain.repository.QuestionOptionRepository;
import kr.co.fastcampus.onboarding.hyeongminonboarding.domain.repository.QuestionRepository;
import kr.co.fastcampus.onboarding.hyeongminonboarding.domain.repository.SurveyRepository;
import kr.co.fastcampus.onboarding.hyeongminonboarding.global.dto.request.BaseRequest;
import kr.co.fastcampus.onboarding.hyeongminonboarding.global.exception.enums.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class SurveyService implements ISurveyService {

    private final AnswerRepository answerRepository;
    private final QuestionOptionRepository questionOptionRepository;
    private final QuestionRepository questionRepository;
    private final SurveyRepository surveyRepository;

    @Transactional
    @Override
    public SurveyCreateResponse createSurvey(BaseRequest<SurveyCreateRequest> request) {

        SurveyCreateRequest req = request.getBody();

        // Valid Check Area
        // 1. Question Type 에따라 QuestionOption 이 반드시 있어야 함.
        req.getQuestionItems().stream().filter(f-> f.getType() == QuestionType.SINGLE_CHOICE || f.getType() == QuestionType.MULTIPLE_CHOICE).forEach(item -> {
            if(CollectionUtil.isEmpty(item.getQuestionOptionItems())){
                throw new SurveyException(ErrorCode.SURVEY_QUESTION_OPTION_EMPTY);
            }
        });


        // Valid Check Area End


        // Service Logical Area

        Survey survey = Survey.builder()
                .name(req.getName())
                .description(req.getDescription())
                .build();

        List<Question> questions = req.getQuestionItems().stream()
                .map(item -> {
                    Question q = Question.builder()
                            .title(item.getTitle())
                            .detail(item.getDetail())
                            .type(item.getType())
                            .required(item.isRequired())
                            .build();

                    if(q.getType() == QuestionType.MULTIPLE_CHOICE
                            || q.getType() == QuestionType.SINGLE_CHOICE){
                        q.setOptions(item.getQuestionOptionItems().stream().map(
                                option -> {
                                    return QuestionOption.builder()
                                            .question(q)
                                            .optionValue(option.getOptionValue())
                                            .build();
                                }
                        ).collect(Collectors.toList()));
                    }
                    q.setSurvey(survey);
                    return q;
                })
                .collect(Collectors.toList());

        survey.setQuestions(questions);
        Survey saved = surveyRepository.save(survey);

        // Service Logical Area End

        return SurveyCreateResponse.builder()
                .surveyId(saved.getId())
                .build();
    }
}
