package fc.icd.jaehyeononboarding.answer.service;

import fc.icd.jaehyeononboarding.answer.model.dto.AnswerCreateDTO;
import fc.icd.jaehyeononboarding.answer.model.dto.AnswerRetrieveDTO;
import fc.icd.jaehyeononboarding.common.model.ApiResponse;
import fc.icd.jaehyeononboarding.common.model.NoDataResponse;

import java.util.List;

public interface AnswerService {
    NoDataResponse createAnswer(Long surveyId, AnswerCreateDTO dto);

    ApiResponse<List<AnswerRetrieveDTO>> getAnswers(Long surveyId);
}
