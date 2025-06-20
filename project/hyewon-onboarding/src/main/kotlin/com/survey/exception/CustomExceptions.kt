package com.survey.exception

/**
 * 각 예외 케이스별 하위 클래스 생성
 */

/**
 * API01
 */
// EXCP01: 항목 수가 0개 또는 10개 초과일 경우
class SurveyItemCountException : RuntimeException("항목 수는 1개 이상 10개 이하이어야 합니다.")
// EXCP02: type이 “single_choice” 또는 “multiple_choice”인데 options가 누락된 경우
class SurveyOptionsMissingException : RuntimeException("선택형 항목에는 options 필드가 필요합니다.")
// EXCP03: 필수 필드 누락 시
class RequiredFieldMissingException : RuntimeException("필수 항목 누락")

/**
 * API02
 */
// EXCP04: 존재하지 않는 설문 ID
class SurveyNotFoundException : RuntimeException("존재하지 않는 설문 ID입니다.")
// EXCP05: 항목 정보 누락
class SurveyUpdateFieldMissingException : RuntimeException("항목 정보가 누락되었습니다.")
// EXCP06: 기존 응답과 호환되지 않는 타입 변경 시
class SurveyTypeConflictException : RuntimeException("기존 응답과 호환되지 않는 타입 변경입니다.")

/**
 * API03
 */
// EXCP07: 필수 항목 누락
class ResponseRequiredFieldMissingException : RuntimeException("필수 항목 누락")
// EXCP08: 허용되지 않은 선택지 응답
class ResponseOptionNotAllowedException : RuntimeException("허용되지 않은 선택지 응답입니다.")
// EXCP09: 응답 데이터 타입 불일치
class ResponseTypeMismatchException : RuntimeException("응답 데이터 타입이 올바르지 않습니다.")

/**
 * API04
 */
// EXCP10: 설문 ID 존재하지 않음
class ResponseSurveyNotFoundException : RuntimeException("존재하지 않는 설문 ID입니다.")

/**
 * API05
 */
// EXCP11: 검색 조건 필드 또는 연산자 오류
class SearchConditionInvalidException : RuntimeException("검색 조건 필드 또는 연산자가 올바르지 않습니다.")
// EXCP12: 숫자 필드 아닌데 lt/gt 같은 범위 비교 조건
class SearchOperatorTypeMismatchException : RuntimeException("비교 연산자는 숫자 필드에만 사용할 수 있습니다.")


/**
 * API07
 */
// EXCP 15: 비활성화된 옵션을 다시 활성화 하는 경우
class SurveyOptionReactivationException: RuntimeException("비활성화된 옵션은 다시 활성화할 수 없습니다.")