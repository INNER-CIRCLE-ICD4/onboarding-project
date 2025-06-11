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