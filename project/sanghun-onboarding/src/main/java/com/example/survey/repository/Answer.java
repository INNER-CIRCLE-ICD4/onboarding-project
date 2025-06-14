package com.example.survey.repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.example.survey.domain.survey.Survey;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor( access = AccessLevel.PROTECTED )
public class Answer {
	
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
	
    @ManyToOne
    private Survey survey;	// 설문조사
	
    @OneToMany(mappedBy = "answer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AnswerItem> answerItems = new ArrayList<>(); // 설문조사 항목
	 
	private LocalDateTime insertDt;	// 생성일자
	private LocalDateTime updateDt;	// 수정일자
	 
}
