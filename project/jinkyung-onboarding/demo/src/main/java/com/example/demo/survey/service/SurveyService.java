package com.example.demo.survey.service;

import com.example.demo.item.Item;
import com.example.demo.item.Repository.ItemRepository;
import com.example.demo.item.Repository.ItemaQuestionRepository;
import com.example.demo.item.dto.CreateItemDto;
import com.example.demo.survey.Dto.CreateSurveyDTO;
import com.example.demo.survey.Repository.SurveyRepository;
import com.example.demo.survey.Survey;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class SurveyService {

    private final ItemRepository itemRepository;

    private final SurveyRepository surveyRepository;

    private final ItemaQuestionRepository itemaQuestionRepository;

    public HttpStatus create(CreateSurveyDTO createSurveyDTO){

        if(createSurveyDTO.getItems().isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No items to create");
        };

        List<Item> items = createSurveyDTO.getItems()
                            .stream()
                            .map(CreateItemDto::toEntity)
                            .collect(Collectors.toList());
        items.stream()
            .filter(item -> item.getItemQuestion() != null)
            .map(item -> itemaQuestionRepository.saveAll(item.getItemQuestion()))
            .collect(Collectors.toList());

        itemRepository.saveAll(items);

        Survey survey = createSurveyDTO.toEntity(items);
        surveyRepository.save(survey);
        return HttpStatus.OK;
    }


}
