package com.example.demo.answer.service;


import com.example.demo.answer.domain.Answer;
import com.example.demo.answer.domain.ItemAnswer;
import com.example.demo.answer.domain.dto.CreateAnswerDto;
import com.example.demo.answer.repository.AnswerRepository;
import com.example.demo.survey.domain.Survey;
import com.example.demo.survey.repository.SurveyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnswerService {

    private final SurveyRepository surveyRepository;

    private final AnswerRepository answerRepository;

    public String createAnswer(Survey survey, CreateAnswerDto createAnswerDto) {

        Answer answer = createAnswerDto.toEntity(survey);
        if(createAnswerDto.getItemAnswers()!=null){
            List<ItemAnswer> itemAnswers= createAnswerDto.getItemAnswers()
                    .stream()
                    .map(
                            itemAnswerDto -> {
                                ItemAnswer itemAnswer = itemAnswerDto.toEntity();
                                itemAnswer.setAnswer(answer);
                                return itemAnswer;
                            }
                    )
                    .collect(Collectors.toList());
            answer.setItemAnswers(itemAnswers);
        }
        answerRepository.save(answer);

        return "응답이 성공적으로 등록되었습니다: ";
    }
}
