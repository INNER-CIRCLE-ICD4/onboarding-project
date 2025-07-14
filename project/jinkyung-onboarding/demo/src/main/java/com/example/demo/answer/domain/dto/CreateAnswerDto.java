package com.example.demo.answer.domain.dto;

import com.example.demo.answer.domain.Answer;
import com.example.demo.answer.domain.ItemAnswer;
import com.example.demo.survey.domain.Survey;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateAnswerDto {

    //답변자 이름
    private String name;

    //답변 항목
    private List<CreateItemAnswerDto> itemAnswers;


    public Answer toEntity(Survey survey) {
        return Answer.builder()
                .name(this.name)
                .survey(survey)
                .build();
    }
}
