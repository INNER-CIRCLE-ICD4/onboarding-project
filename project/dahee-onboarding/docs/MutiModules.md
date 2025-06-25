#멀티모듈

1. 관심사 분리(Separation of Concerns)

요구사항 분석서에 “프로젝트 구성 방법 및 시스템 아키텍처 설계”가 평가 기준이었죠.
survey-core는 도메인 모델(Entity, Repository) 만 담고,
survey-service는 비즈니스 로직(Service Layer) 만,
survey-api는 입출력(Controller, DTO, Validation) 만 책임지도록 분리했으니,

“각 레이어가 자신의 역할만 집중”하는 이상적인 레이어드 아키텍처(Layered Architecture)를 충실히 구현했습니다.

2. 유지보수성(Maintainability)

기능이 늘어나거나, 검증로직·Exception 포맷이 바뀌더라도 영향 범위가 모듈에 국한됩니다.

3. 테스트 용이성(Testability)

survey-core만 따로 JPA 단위 테스트,
survey-service만 Mockito 기반 단위 테스트,
survey-api만 Spring MVC 테스트… 이렇게 각 모듈별로 테스트 전략을 분리할 수 있어요.

# 구조

1. common : 공통 예외(ApiException), 유틸(공토암수, DTO 매핑)
2. survey-core : JPA Entity, Repository
- 순수 JPA 엔티티/리포지토리 라이브러리,
- springframework 플러그인 적용 시 Spring Boot 애플리케이션처럼 부팅, 패키징 설정을 갖게 되기 때문에 라이브러리 모듈을 일반 JAR로 남겨줌
- io.spring.dependency-management + mavenBom 조합으로 버전 관리에만 집중
- Boot 플러그인 붙이지 않고 BOM을 직접 import하여 버전 관리
3. survey-service : 비즈니스 로직, 트랜잭션
- Boot 플러그인을 붙여 버전 관리 자동화 진행
4. survet-api : REST Controller , DTO, Validation Devtools


