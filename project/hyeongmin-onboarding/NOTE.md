# 2025-06-10 [v0.0.1]
## Changed or Added
| ID 값 변경
> Entity의 ID 값들이 @GeneratedValue(strategy = GenerationType.IDENTITY) 이어서 
> Transaction을 지원하는 쓰기 지연 방식이 동작하지 않으므로 변경이 필요하다고 인식
> 다중환경(DB 분산 시) 일 경우에는 UUID 나 Snowflake 사용하는게 확장성 높으므로
> Snowflake 모듈을 사용하여 AOP 작성, save* 로 캐치하여 id 값이 있을때 0 또는 null이면 값을 채우도록 함

| Repository 추가 
> QueryDsl 사용으로 RepositoryCustomImpl 구현체 생성
> 테스트 케이스 작성하면서 채울 예정

## fixed


## etc

# 2025-06-11 [v0.0.1]
## Changed or Added
| Exception Handler 추가
> Exception 발생 시, 응답 구조를 컨트롤 하기 위해 추가
> 사용자에게 보여 줄 메시지는 ErrorCode에 작성한 message를 보여주도록 하고
> 내부적으로 Exception message 를 systemMessage 에 담아서 리턴하여 디버깅을 원할하게 함
> Exception 발생 시 원하는 구조로 응답이 오는지 테스트 하기위해 테스트 케이스 작성
> Controller를 만들지 않아도 테스트 할 수 있을까 찾아봤지만 테스트용으로 Controller 만들고 하는게 가장 쉽고 깔끔

| Survey Create API 추가
> 설문조사 생성 API 추가 완료
> 단순 Save 이므로 JPA Repository으로 처리 

## fixed
| Snowflake AOP 이슈 
> Snowflake가 오직 Spring Data 리포지토리(save(...)) 호출만 가로채기 때문에
> surveyRepository.save(survey) 할 때 전달된 Survey 엔티티에만 ID를 채워주는 현상을 테스트케이스에서 발견
> questions와 그 안의 QuestionOption들은 모두 JPA의 cascade persist 로 
> EntityManager.persist()를 통해 직접 저장되기 때문에, 리포지토리의 save(...) 메서드를 거치지 않는 사실을 알게됨
> 때문에 @onPrePersist (JPA 라이프사이클 콜백)을 사용하여 JPA 가 persist 하기 직전에 ID를 Snowflake 로 채우는 로직으로 변경
> SnowflakeIdAspect 삭제 후 SnowflakeIdListener 생성


# 2025-06-11 [v0.0.2]
## refactor
- JPA 의 연관관게 (OneToMany) 사용하지 않도록 다시 모델링 작업함
- 만든 모델링으로 전체적인 데이터 테이스 케이스 작성함
- Snapshot 기반 데이터 체계 구현
  - 설문조사 업데이트 시, Survey Version UP
  - 모든 설문조사 응답 제출시, 질문과 응답은 각각 스냅샷으로 변경되어 디비에 저장
  - 설문조사 응답을 조회하면 각각의 응답은 기존 질문과 응답을 스냅샷으로 가지고있음
- DTO 기반 데이터 조립 Assembler 생성
  - 모든 Entity의 조립은 Assembler 에서 담당하도록 함
- 설문조사 제출시, 각각의 응답에는 질문타입이 있으므로 타입별 validation 체크와 Entity 조립을 위해 AnswerHandler 인터페이스 추가
  - 추후 설문조사 응답에 타입이 추가되더라도 구현체를 하나 추가하면 되도록 함


# 2025-06-11 [v0.0.3]
## refactor
- Assembler 를 범용적으로, Context 추가시 소스 변경 없이 사용 할 수 있도록 변경
  - AssemblyContext :  DTO를 만들기 위해 필요한 모든 데이터(Entity, 값 객체 등)를 담는 컨테이너
  - AssemblerFactory : 어떤 DTO를 조립할지 결정하고, 알맞은 Assembler를 찾아 실행
  - ContextKey<T> : AssemblyContext 내부에서 값을 식별하고, 꺼낼 때 타입 캐스팅을 보장하기 위한 Key 클래스
  - Assembler<D> : DTO 한 종류를 조립하는 책임을 가지는 전략 객체
  ```
     Survey survey = Survey.builder()
                .title(req.getTitle())
                .description(req.getDescription())
                .version(1)
                .build();

        return assemblerFactory.assemble(SurveyResponseDto.class, assemblyContext -> {
            assemblyContext.put(SURVEY_CONTEXT_KEY, survey);
        });
  ```
