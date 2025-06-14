package com.INNER_CIRCLE_ICD4.innerCircle.service;

import com.INNER_CIRCLE_ICD4.innerCircle.domain.*;
import com.INNER_CIRCLE_ICD4.innerCircle.dto.*;
import com.INNER_CIRCLE_ICD4.innerCircle.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ResponseService {

    private final SurveyRepository surveyRepository;
    private final QuestionRepository questionRepository;
    private final ChoiceRepository choiceRepository;
    private final ResponseRepository responseRepository;

    // ✅ 응답 저장
    public UUID saveResponse(ResponseRequest request) {
        Survey survey = surveyRepository.findById(request.surveyId())
                .orElseThrow(() -> new IllegalArgumentException("설문이 존재하지 않습니다."));

        Response response = new Response(survey, "snapshot data"); // 실제 스냅샷은 추후 개선

        for (AnswerRequest ar : request.answers()) {
            Question question = questionRepository.findById(ar.questionId())
                    .orElseThrow(() -> new IllegalArgumentException("질문이 존재하지 않습니다."));

            // ✅ 필수 질문 유효성 검사
            boolean isTextBlank = ar.textValue() == null || ar.textValue().isBlank();
            boolean isChoiceEmpty = ar.selectedChoiceIds() == null || ar.selectedChoiceIds().isEmpty();

            if (question.isRequired() && isTextBlank && isChoiceEmpty) {
                throw new IllegalArgumentException("필수 질문에는 응답이 필요합니다.");
            }

            Answer answer = new Answer(question, ar.textValue());

            // ✅ 선택지 유효성 검사
            if (ar.selectedChoiceIds() != null) {
                for (UUID choiceId : ar.selectedChoiceIds()) {
                    Choice choice = choiceRepository.findById(choiceId)
                            .orElseThrow(() -> new IllegalArgumentException("선택지가 존재하지 않습니다."));

                    if (!choice.getQuestion().getId().equals(question.getId())) {
                        throw new IllegalArgumentException("선택지는 해당 질문에 속하지 않습니다.");
                    }

                    answer.addChoice(new AnswerChoice(choice));
                }
            }

            response.addAnswer(answer);
        }

        responseRepository.save(response);
        return response.getId();
    }

    // ✅ 설문에 속한 모든 응답 조회
    public List<ResponseDto> findAllBySurveyId(UUID surveyId) {
        List<Response> responses = responseRepository.findAllBySurvey_Id(surveyId);
        return responses.stream()
                .map(ResponseDto::from)
                .toList();
    }

    // ✅ 개별 응답 ID로 상세 조회
    public ResponseDto findById(UUID responseId) {
        Response response = responseRepository.findById(responseId)
                .orElseThrow(() -> new IllegalArgumentException("응답이 존재하지 않습니다."));
        return ResponseDto.from(response);
    }
}
