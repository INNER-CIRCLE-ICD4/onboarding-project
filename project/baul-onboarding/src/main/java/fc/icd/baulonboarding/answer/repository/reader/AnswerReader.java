package fc.icd.baulonboarding.answer.repository.reader;

import fc.icd.baulonboarding.answer.model.entity.Answer;

public interface AnswerReader {

    Answer getAnswerBy(Long answerId);

}
