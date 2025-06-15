package com.multi.sungwoongonboarding.responses.application;

import com.multi.sungwoongonboarding.questions.application.repository.QuestionRepository;
import com.multi.sungwoongonboarding.questions.domain.Questions;import com.multi.sungwoongonboarding.responses.domain.Answers;
import com.multi.sungwoongonboarding.responses.domain.Responses;
import com.multi.sungwoongonboarding.responses.application.repository.ResponseRepository;
import com.multi.sungwoongonboarding.responses.dto.ResponseCreateRequest;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
@RequiredArgsConstructor
public class ResponseService {

    private final ResponseRepository responseRepository;
    private final QuestionRepository questionService;
    private final Validator validator;

    @Transactional
    public void submitResponse(ResponseCreateRequest request) {

        // 응답 저장
        Responses response = request.toDomain();

        response.getAnswers().forEach(answer -> {

            Questions originalQuestion = questionService.findById(answer.getQuestionId());
            answer.setOriginalQuestion(originalQuestion);

        });

        Responses savedResponse = responseRepository.save(response);
    }
}
