package com.tommy.jaeyoungonboarding.service;

import com.tommy.jaeyoungonboarding.entity.Survey;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SurveyService {

    /*
    * 모든 Survey 조회
    * @return Survey 리스트
    * */
    public List<Survey> selectAllSurvey(){

        List<Survey> selectAllSurvey = surveyRepository.selectAllSurvey();

        if(selectAllSurvey.isEmpty()){

        }

        return null;
    }
}
