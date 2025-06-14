## 주요 설계 원칙

### 계층 간 의존성 규칙
- 외부 계층에서 내부 계층으로의 단방향 의존성 유지
- 모든 의존성은 안쪽으로 향해야 함 (Domain Layer 방향)
- Domain Layer는 어떤 외부 계층에도 의존하지 않음

### 관심사의 분리
- Presentation: HTTP 요청/응답 처리와 데이터 변환
- Application: 비즈니스 usecase 오케스트레이션
- Domain: 핵심 비즈니스 규칙과 도메인 로직
- Infrastructure: 영속성 처리를 위한 인터페이스 정의
- Repository: 실제 데이터 저장소 구현과 엔티티 관리

### 의존성 역전
- Infrastructure Layer에서 Port(Repository Interface) 정의
- Repository Layer에서 Adapter(Repository Implementation) 구현
- Service는 Infrastructure Layer의 Port를 통해 저장소와 통신
- 실제 구현체는 Repository Layer에서 Port를 구현하여 제공
