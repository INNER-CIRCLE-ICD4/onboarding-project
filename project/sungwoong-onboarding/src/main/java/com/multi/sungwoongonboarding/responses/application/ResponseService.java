package com.multi.sungwoongonboarding.responses.application;

import com.multi.sungwoongonboarding.questions.application.repository.QuestionRepository;
import com.multi.sungwoongonboarding.responses.application.repository.AnswerRepository;
import com.multi.sungwoongonboarding.responses.domain.Responses;
import com.multi.sungwoongonboarding.responses.application.repository.ResponseRepository;
import com.multi.sungwoongonboarding.responses.dto.ResponseCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class ResponseService {

    private final ResponseRepository responseRepository;
    private final AnswerRepository answerRepository;
    private final QuestionRepository questionService;

    @Transactional
    public void submitResponse(ResponseCreateRequest request) {
        // 응답 저장
        Responses response = request.toDomain();
        response.getAnswers().forEach(answer -> {
            answer.setOriginalQuestion(questionService.findById(answer.getQuestionId()));
        });
        Responses savedResponse = responseRepository.save(response);
    }
}
