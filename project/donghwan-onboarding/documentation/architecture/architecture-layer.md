## 계층 설명

### Presentation Layer
- Controller: 외부 요청 처리 및 응답 변환을 담당
- DTO: API 요청/응답에 사용되는 데이터 전송 객체

### Application Layer
- Service Interface: 비즈니스 유스케이스 정의 및 추상화
- Service: 실제 비즈니스 로직 구현 및 도메인 모델 조작

### Domain Layer
- Domain Model: 핵심 비즈니스 규칙과 상태를 포함하는 도메인 객체

### Infrastructure Layer
- Repository Port: 영속성 처리를 위한 인터페이스 정의
- Projection Model: 조회를 위한 Projection 객체

### Repository Layer
- Repository Implementation: 실제 데이터 저장소 구현체
- Entity: 데이터베이스 테이블과 매핑되는 영속성 객체