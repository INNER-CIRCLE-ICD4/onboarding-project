package fc.icd.baulonboarding.answer.service;

import fc.icd.baulonboarding.answer.model.dto.AnswerDto;

public interface AnswerService {

    void registerAnswer(AnswerDto.RegisterAnswerRequest request);
}
