package com.example.survey.domain.survey;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // JPA 기본 생성자 필수
public class Item {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;				// 아이디
    
    private int sn;					// 순번(이력관리)

    private String name;			// 설문조사 이름

    private String description;		// 설문조사 설명

    @OneToMany(mappedBy = "survey", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Item> items = new ArrayList<>();

    
    /**
     * @Column 생략 가능 
     * 네이밍 관례
     * -java camelCase (낙타표기법)
     * -db	snakeCase
     * 
     * createdAt 필드는 JPA 매핑 시 기본적으로 변수명 그대로 DB 컬럼명으로 매핑된다.
     * 예를 들어, 이 필드는 DB의 'createdAt' 컬럼에 매핑됨.
     *
     * 하지만 application.yml에서 다음 설정을 적용하면,
     * CamelCase → snake_case 자동 변환이 가능하다:
     *
     * spring:
     *   jpa:
     *     hibernate:
     *       naming:
     *         physical-strategy: org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl
     *
     * 또는 Spring Boot 2.1 이상에서는 기본적으로 해당 네이밍 전략이 적용되므로,
     * 별도 설정 없이도 insertDt → insert_dt 로 자동 매핑된다.
     *
     * 명시적으로 컬럼명을 지정하고 싶을 경우엔 아래와 같이 사용할 수 있다:
     * @Column(name = "insert_dt")
     */
    private LocalDateTime insertDt;	// 생성일자
    
    @Column( name = "update_dt" )
    private LocalDateTime updateDt; 	// 수정일자
}
