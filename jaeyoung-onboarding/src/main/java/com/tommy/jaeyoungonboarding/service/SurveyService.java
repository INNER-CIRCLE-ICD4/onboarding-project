package com.tommy.jaeyoungonboarding.service;

import com.tommy.jaeyoungonboarding.dto.CreateSurveyDTO;
import com.tommy.jaeyoungonboarding.entity.Survey;
import com.tommy.jaeyoungonboarding.entity.SurveyItem;
import com.tommy.jaeyoungonboarding.exception.ResourceNotFoundException;
import com.tommy.jaeyoungonboarding.repository.SurveyItemRepository;
import com.tommy.jaeyoungonboarding.repository.SurveyRepository;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class SurveyService {

    private final SurveyRepository surveyRepository;
    private final SurveyItemRepository surveyItemRepository;

    /*
    * 모든 Survey 조회
    * @return Survey 리스트
    * @exception selectAllSurvey.isEmpty 시 ResourceNotFoundException 발생
    * */
    public List<Survey> selectAllSurvey(){

        List<Survey> selectAllSurvey = surveyRepository.findAll();

        if(selectAllSurvey.isEmpty()){
            throw new ResourceNotFoundException("생성한 설문조사가 없습니다.");
        }
        return selectAllSurvey;
    }

    /*
    * Survey 생성
    * @return Survey 생성 상태
    * 하나의 트랜잭션으로 실행
    * */
    @Transactional
    public String createSurvey(CreateSurveyDTO createSurveyDTO){
        log.info("CreateSurvey Service 진입...");

        Survey survey = Survey
                .builder()
                .surveyTitle(createSurveyDTO.getTitle())
                .surveyDescription(createSurveyDTO.getDescription())
                .build();
        surveyRepository.save(survey);

        log.info("Survey 객체 생성 완료");

        SurveyItem surveyItem = SurveyItem
                .builder()
                .surveyItemTitle(createSurveyDTO.getSurveyItemTitle())
                .surveyItemDescription(createSurveyDTO.getSurveyItemDescription())
                .surveyItemForm(createSurveyDTO.getSurveyItemForm())
                .surveyItemEssential(createSurveyDTO.isSurveyItemEssential())
                .survey(survey)
                .build();
        surveyItemRepository.save(surveyItem);

        log.info("SurveyItem 객체 생성 완료");

        return survey.getSurveyId().toString();
    }
}
