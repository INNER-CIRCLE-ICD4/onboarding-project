package com.example.survey.repository;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor( access = AccessLevel.PROTECTED )
public class AnswerItem {
	
	/*
	 * @NoArgsConstructor( access = AccessLevel.PROTECTED)
	 * 사용 이유 : 도메인은 테이블과 1:1 무결성 유지해야함
	 * 해당 속성 사용안할시 외부에서 무분별하게 new () 객체를 생성 가능
	 * -> 무결성이 꺠지고, 디버깅 어렵고, 나중에 어디서 잘못됐는지 추적이 힘듬
	 * 
	 * */
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long answerId;	// 응답 아이디

    @ManyToOne(fetch = FetchType.LAZY)
    private Answer answer;
	
    private Long surveyitemId; // 설문조사 항목 아이디 (surveyItem과 매칭)
    private int surveyitemSn;  // 설문조사 항목 순번  (surveyItem과 매칭)
    
    private String answerData;	// 응답값
	//	 private 
	private LocalDateTime insertDt;	// 생성일자
	private LocalDateTime updateDt;	// 수정일자
	 
}
