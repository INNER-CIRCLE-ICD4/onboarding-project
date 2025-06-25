#Entity 구성
Survey ↔ SurveyItem : “버전 + Soft Delete” 기반 구조 관리
SurveyResponse ↔ SurveyResponseItem : “응답 그룹화 + 텍스트 스냅샷” 구현
QuestionType Enum : “입력 형태”를 명확히 제한

#텍스트 스냅샷
문항 텍스트만 응답 레코드에 복사해 두는 경량화된 방식
→ 장점: 제목·내용 변경에도 과거 응답의 “원래 질문” 보존
→ 단점: 옵션 변경에는 직접 대응 불가


#구현 할 핵심 기능
1.설문 생성 (POST /api/surveys)
DTO → Survey(version=1) + SurveyItem(isDeleted=false) 저장

2.설문 수정 (PUT /api/surveys/{code})
code로 최신 Survey 조회 → 복제 버전 생성(version++) → 문항 추가/삭제(isDeleted 플래그)

3.설문 응답 제출 (POST /api/surveys/{id}/responses)
Survey(id), SurveyItem 로드 → 필수/옵션 검증 → SurveyResponse + SurveyResponseItem(questionText 복제) 저장

4.설문 응답 조회 (GET /api/surveys/{id}/responses)
페이징 + 정렬 + questionText 포함 응답 반환

5.글로벌 예외 처리 (@ControllerAdvice)
NotFound, BadRequest 등의 공통 에러 포맷 통일

6.단위·통합 테스트
Service 레이어 유효성 → JUnit/Kotest
컨트롤러 + H2 통합 테스트 → SpringBootTest