package fc.icd.jaehyeononboarding.answer.service;

import fc.icd.jaehyeononboarding.answer.model.dto.AnswerCreateDTO;
import fc.icd.jaehyeononboarding.answer.model.dto.AnswerRetrieveDTO;
import fc.icd.jaehyeononboarding.answer.model.dto.AnswerWithQuestionDTO;
import fc.icd.jaehyeononboarding.answer.model.entity.Answer;
import fc.icd.jaehyeononboarding.answer.repository.AnswerRepository;
import fc.icd.jaehyeononboarding.common.constants.ResultCodes;
import fc.icd.jaehyeononboarding.common.exception.ApiCommonException;
import fc.icd.jaehyeononboarding.common.model.ApiResponse;
import fc.icd.jaehyeononboarding.common.model.NoDataResponse;
import fc.icd.jaehyeononboarding.survey.model.entity.QuestionGroup;
import fc.icd.jaehyeononboarding.survey.model.entity.Survey;
import fc.icd.jaehyeononboarding.survey.repository.QuestionGroupRepository;
import fc.icd.jaehyeononboarding.survey.repository.SurveyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AnswerServiceImpl implements AnswerService {

    private final AnswerRepository answerRepository;
    private final SurveyRepository surveyRepository;

    @Transactional
    @Override
    public NoDataResponse createAnswer(Long surveyId, AnswerCreateDTO dto) {

        Long questionGroupId = dto.getQuestionGroupId();
        QuestionGroup questionGroup = new QuestionGroup();
        questionGroup.setId(questionGroupId);
        List<?> answerList = dto.getAnswersWithQuestion().stream()
                .map(AnswerWithQuestionDTO::getContent)
                .toList();
        Answer answer = Answer.create(questionGroup, answerList);

        answerRepository.save(answer);

        return new NoDataResponse();
    }

    @Transactional(readOnly = true)
    @Override
    public ApiResponse<List<AnswerRetrieveDTO>> getAnswers(Long surveyId) {

        Survey survey = surveyRepository.findById(surveyId).orElseThrow(() -> new ApiCommonException(ResultCodes.RC_20010));

        List<Long> QuestionGroupList = survey.getQuestionGroups().stream().map(QuestionGroup::getId).toList();

        List<Answer> allAnswersForSurvey = answerRepository.findByQuestionGroupIdIn(QuestionGroupList);

        List<AnswerRetrieveDTO> answerData = allAnswersForSurvey.stream().map(AnswerRetrieveDTO::from).toList();


        return new ApiResponse<>(ResultCodes.RC_10000, answerData);
    }
}
