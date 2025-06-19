package fc.icd.baulonboarding.answer.repository.reader;

import fc.icd.baulonboarding.answer.model.entity.Answer;
import fc.icd.baulonboarding.answer.repository.entity.AnswerRepository;
import fc.icd.baulonboarding.common.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AnswerReaderImpl implements AnswerReader {

    private final AnswerRepository answerRepository;

    @Override
    public Answer getAnswerBy(Long answerId) {
        return answerRepository.findById(answerId)
                .orElseThrow(() -> new EntityNotFoundException("Answer (ID: " + answerId + " 에 해당하는 설문응답이 존재하지 않습니다.)"));
    }

}
