package fc.icd.jaehyeononboarding.answer.service;

import fc.icd.jaehyeononboarding.answer.model.dto.AnswerCreateDTO;
import fc.icd.jaehyeononboarding.answer.model.dto.AnswerWithQuestionDTO;
import fc.icd.jaehyeononboarding.answer.model.entity.Answer;
import fc.icd.jaehyeononboarding.answer.repository.AnswerRepository;
import fc.icd.jaehyeononboarding.common.model.NoDataResponse;
import fc.icd.jaehyeononboarding.survey.model.entity.QuestionGroup;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AnswerServiceImpl implements AnswerService {

    private final AnswerRepository answerRepository;

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
}
