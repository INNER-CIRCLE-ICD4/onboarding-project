package fc.icd.baulonboarding.answer.service;

import fc.icd.baulonboarding.answer.model.dto.AnswerCommand;
import fc.icd.baulonboarding.answer.model.dto.AnswerInfo;

public interface AnswerService {

    void registerAnswer(AnswerCommand.RegisterAnswer registerAnswer);

    AnswerInfo.Answer retrieveAnswer(Long answerId);

}
