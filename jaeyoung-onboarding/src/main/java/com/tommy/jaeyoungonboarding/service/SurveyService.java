package com.tommy.jaeyoungonboarding.service;

import com.tommy.jaeyoungonboarding.entity.Survey;
import com.tommy.jaeyoungonboarding.exception.ResourceNotFoundException;
import com.tommy.jaeyoungonboarding.repository.SurveyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SurveyService {

    private final SurveyRepository surveyRepository;

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
}
