package inner.circle.boram_onboarding.survay.service;

import inner.circle.boram_onboarding.survay.dto.SurveyAnswerResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import inner.circle.boram_onboarding.survay.dto.SurveyResponseSubmitRequest;
import inner.circle.boram_onboarding.survay.entity.*;
import inner.circle.boram_onboarding.survay.repository.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ResponseService {
    private final SurveyRepository surveyRepository;
    private final ResponseRepository responseRepository;
    private final AnswerRepository answerRepository;

    @Transactional
    public Long submitResponse(Long surveyId, SurveyResponseSubmitRequest request) {
        Survey survey = surveyRepository.findById(surveyId)
                .orElseThrow(() -> new RuntimeException("설문 없음"));

        Response response = new Response();
        response.setSurvey(survey);
        response.setSubmittedAt(LocalDateTime.now());

        // 응답에 대한 답변(Answer) 생성
        List<Answer> answerList = request.getAnswers().stream()
                .map(a -> {
                    Answer answer = new Answer();
                    answer.setResponse(response);
                    answer.setQuestionName(a.getQuestionName());
                    // value: 단답형/장문형은 String, 선택형은 JSON 또는 콤마구분 String 등으로 저장
                    if (a.getValue() instanceof List) {
                        answer.setValue(String.join(",", (List<String>)a.getValue()));
                    } else {
                        answer.setValue(a.getValue().toString());
                    }
                    return answer;
                }).collect(Collectors.toList());
        response.setAnswers(answerList);

        responseRepository.save(response);

        return response.getId();
    }

    @Transactional(readOnly = true)
    public List<SurveyAnswerResponse> getResponses(Long surveyId) {
        List<Response> responseEntities = responseRepository.findBySurveyId(surveyId);
        // 엔티티 → DTO 변환 (stream/map 등으로 변환)
        return responseEntities.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }


    // 엔티티 → DTO 변환 메서드
    private SurveyAnswerResponse toDto(Response response) {
        SurveyAnswerResponse dto = new SurveyAnswerResponse();
        dto.setResponseId(response.getId());
        dto.setSubmittedAt(response.getSubmittedAt().toString());

        List<SurveyAnswerResponse.AnswerDto> answers = response.getAnswers().stream()
                .map(answer -> {
                    SurveyAnswerResponse.AnswerDto adto = new SurveyAnswerResponse.AnswerDto();
                    adto.setQuestionName(answer.getQuestionName());
                    // 선택형(여러 값) 처리: 필요시 value를 List<String>으로 변환
                    adto.setValue(answer.getValue()); // 단순 String이지만, 필요에 따라 분기
                    return adto;
                })
                .collect(Collectors.toList());

        dto.setAnswers(answers);
        return dto;
    }


}
