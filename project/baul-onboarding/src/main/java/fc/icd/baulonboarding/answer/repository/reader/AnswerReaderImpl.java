package fc.icd.baulonboarding.answer.repository.reader;

import fc.icd.baulonboarding.answer.model.entity.Answer;
import fc.icd.baulonboarding.answer.repository.entity.AnswerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AnswerReaderImpl implements AnswerReader {

    private final AnswerRepository answerRepository;

    @Override
    public Answer getAnswerBy(Long answerId) {
        return answerRepository.findById(answerId)
                .orElseThrow(RuntimeException::new);
    }

}
