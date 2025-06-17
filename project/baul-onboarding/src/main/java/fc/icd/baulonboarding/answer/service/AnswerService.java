package fc.icd.baulonboarding.answer.service;

import fc.icd.baulonboarding.answer.model.dto.AnswerDto;
import fc.icd.baulonboarding.answer.model.dto.AnswerInfo;

public interface AnswerService {

    void registerAnswer(AnswerDto.RegisterAnswerRequest request);

    AnswerInfo.Answer retrieveAnswer(Long answerId);

}
