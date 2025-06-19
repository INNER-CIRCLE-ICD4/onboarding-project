package fc.icd.baulonboarding.answer.repository.store;

import fc.icd.baulonboarding.answer.model.entity.Answer;
import fc.icd.baulonboarding.answer.repository.entity.AnswerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AnswerStoreImpl implements AnswerStore{

    private final AnswerRepository answerRepository;

    @Override
    public Answer store(Answer answer) {
        return answerRepository.save(answer);
    }
}
