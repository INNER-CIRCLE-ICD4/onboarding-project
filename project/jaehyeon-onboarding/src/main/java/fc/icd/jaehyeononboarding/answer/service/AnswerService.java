package fc.icd.jaehyeononboarding.answer.service;

import fc.icd.jaehyeononboarding.answer.model.dto.AnswerCreateDTO;
import fc.icd.jaehyeononboarding.common.model.NoDataResponse;

public interface AnswerService {
    NoDataResponse createAnswer(Long surveyId, AnswerCreateDTO dto);
}
