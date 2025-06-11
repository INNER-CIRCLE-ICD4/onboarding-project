# 아키텍처 설계

## 시스템 아키텍처 개요

```
+------------------+
|  API Layer       |   REST Controllers
+------------------+
|  Service Layer   |   Business Logic
+------------------+
|  Repository Layer|   Data Access
+------------------+
|  Database        |   H2 In-Memory Database
+------------------+
```

## 계층별 상세 설명

### API 계층 (Controller Layer)
- REST 엔드포인트 제공
- 요청 및 응답의 유효성 검사
- 적절한 HTTP 상태 코드 반환
- 서비스 계층에 비즈니스 로직 위임

### 서비스 계층 (Service Layer)
- 비즈니스 로직 구현
- 트랜잭션 관리
- 데이터 일관성 검증
- 리포지토리 계층과의 상호 작용

### 데이터 액세스 계층 (Repository Layer)
- 데이터베이스 작업 수행
- JPA Repository 인터페이스 활용
- 쿼리 메소드 및 JPQL 쿼리 정의

### 데이터베이스
- H2 인메모리 데이터베이스 사용
- 스키마 자동 생성 (Hibernate DDL)
- 개발 환경에서 웹 콘솔 활성화