package com.example.demo.survey.service;

import com.example.demo.item.Item;
import com.example.demo.item.Repository.ItemRepository;
import com.example.demo.item.Repository.ItemaQuestionRepository;
import com.example.demo.item.dto.UpdateItemDto;
import com.example.demo.itemQuestion.ItemQuestion;
import com.example.demo.survey.Dto.CreateSurveyDTO;
import com.example.demo.survey.Dto.SurveyDto;
import com.example.demo.survey.Dto.UpdateSurveyDTO;
import com.example.demo.survey.Repository.SurveyRepository;
import com.example.demo.survey.Survey;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Log4j2
public class SurveyService {

    private final ItemRepository itemRepository;

    private final SurveyRepository surveyRepository;

    private final ItemaQuestionRepository itemaQuestionRepository;

    // 설문조사 api 등록
    public SurveyDto create(CreateSurveyDTO createSurveyDTO){

        if(createSurveyDTO.getItems().isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No items to create");
        };

        Survey survey = surveyRepository.save(
                createSurveyDTO.toEntity(Collections.emptyList())
        );

        AtomicInteger num = new AtomicInteger(0);
        List<Item> items = createSurveyDTO.getItems()
                            .stream()
                            .map(
                                    createItemDto ->{
                                        Item item = createItemDto.toEntity();
                                        item.setItemNo(num.getAndIncrement());
                                        itemRepository.save(item);
                                        // ItemQuestion이 있는 경우에만 질문 항목 저장
                                        if(createItemDto.getItemQuestionList() != null && createItemDto.getItemQuestionList().size() < 10){
                                            item.setItemQuestion(
                                                    createItemDto.getItemQuestionList()
                                                            .stream()
                                                            .map( question ->{
                                                                ItemQuestion itemQuestion = ItemQuestion.toEntity(question);
                                                                itemQuestion.setItem(item);
                                                                itemQuestion.setQuestionNo(num.getAndIncrement());
                                                                return itemaQuestionRepository.save(itemQuestion);
                                                            }
                                                    ).collect(Collectors.toList())
                                            );
                                        }else {
                                            item.setItemQuestion(Collections.emptyList());
                                        }
                                        item.setSurvey(survey);
                                        return item;
                                    }
                            )
                            .collect(Collectors.toList());


        survey.setItems(items);
        return SurveyDto.fromSurvey(surveyRepository.save(survey));
    }

    public Survey findSurvey(Long surveyId){
        return surveyRepository.findById(surveyId)
                .orElseThrow(() -> new RuntimeException("NOT FOUND SURVEY"));
    }

    public SurveyDto update(UpdateSurveyDTO updateSurveyDTO){
        // 설문지 찾기
        Survey updateSurvey = findSurvey(updateSurveyDTO.getSurveyId());
        List<Item> item2= updateSurvey.getItems();

        if(updateSurveyDTO.getItems() != null){
            // 질문 업데이트
            updateSurveyDTO.getItems().forEach(itemDto -> {
                Item item = updateSurvey.getItems().stream()
                        .filter(i -> Objects.equals(i.getItemNo(),itemDto.getItemNo()))
                        .findFirst()
                        .orElseThrow(() -> new IllegalArgumentException("Item not found"));
            item.updateItem(itemDto);

            //질문 항목 업데이트
                itemDto.getItemQuestionList().forEach(itemQuestionDto -> {
                    if(itemQuestionDto.getQuestion() != null){
                        ItemQuestion itemQuestion = item.getItemQuestion().stream()
                                .filter(q -> q.getQuestionNo() == itemQuestionDto.getQuestionNo())
                                .findFirst()
                                .orElseThrow(() -> new IllegalArgumentException("ItemQuestion not found"));
                        itemQuestion.updateItemQuestion(itemQuestionDto);
                    }
                });
            });
        }
        updateSurvey.updateSurvey(updateSurveyDTO);

        return SurveyDto.fromSurvey(updateSurvey);
    }

//    public SurveyDto getSurveyById(Long id){
//
//    }
}
