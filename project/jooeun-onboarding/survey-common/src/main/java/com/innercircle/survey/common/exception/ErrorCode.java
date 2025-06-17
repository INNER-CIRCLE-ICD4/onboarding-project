package com.innercircle.survey.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * 표준화된 에러 코드 정의
 * 
 * 모든 비즈니스 예외에 대한 에러 코드를 중앙에서 관리합니다.
 * Spring 의존성 없이 순수 Java로 구현하여 재사용성을 높였습니다.
 */
@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // ==================== 일반적인 에러 (1000번대) ====================
    INVALID_REQUEST("ERR_1001", "잘못된 요청입니다.", 400),
    INVALID_PARAMETER("ERR_1002", "잘못된 매개변수입니다.", 400),
    VALIDATION_FAILED("ERR_1003", "유효성 검증에 실패했습니다.", 400),
    ACCESS_DENIED("ERR_1004", "접근 권한이 없습니다.", 403),
    RESOURCE_NOT_FOUND("ERR_1005", "요청한 리소스를 찾을 수 없습니다.", 404),
    METHOD_NOT_ALLOWED("ERR_1006", "지원하지 않는 HTTP 메서드입니다.", 405),
    UNSUPPORTED_MEDIA_TYPE("ERR_1007", "지원하지 않는 미디어 타입입니다.", 415),
    INTERNAL_SERVER_ERROR("ERR_1008", "서버 내부 오류가 발생했습니다.", 500),
    
    // ==================== 동시성 관련 에러 (2000번대) ====================
    OPTIMISTIC_LOCK_FAILED("ERR_2001", "다른 사용자가 동시에 수정했습니다. 최신 버전을 다시 조회하여 수정해주세요.", 409),
    RESOURCE_CONFLICT("ERR_2002", "리소스 충돌이 발생했습니다.", 409),
    DUPLICATE_RESOURCE("ERR_2003", "중복된 리소스입니다.", 409),
    
    // ==================== 설문조사 관련 에러 (3000번대) ====================
    SURVEY_NOT_FOUND("ERR_3001", "설문조사를 찾을 수 없습니다.", 404),
    SURVEY_ACCESS_DENIED("ERR_3002", "설문조사에 대한 접근 권한이 없습니다.", 403),
    SURVEY_ALREADY_INACTIVE("ERR_3003", "이미 비활성화된 설문조사입니다.", 400),
    SURVEY_NOT_ANSWERABLE("ERR_3004", "응답할 수 없는 설문조사입니다.", 400),
    SURVEY_QUESTION_COUNT_INVALID("ERR_3005", "설문 항목 개수가 유효하지 않습니다.", 400),
    SURVEY_CREATION_FAILED("ERR_3006", "설문조사 생성에 실패했습니다.", 500),
    SURVEY_UPDATE_FAILED("ERR_3007", "설문조사 수정에 실패했습니다.", 500),
    
    // ==================== 설문 질문 관련 에러 (3100번대) ====================
    QUESTION_NOT_FOUND("ERR_3101", "설문 질문을 찾을 수 없습니다.", 404),
    QUESTION_TYPE_INVALID("ERR_3102", "유효하지 않은 질문 유형입니다.", 400),
    QUESTION_OPTIONS_REQUIRED("ERR_3103", "선택형 질문에는 선택지가 필요합니다.", 400),
    QUESTION_OPTIONS_INVALID("ERR_3104", "선택지가 유효하지 않습니다.", 400),
    
    // ==================== 설문조사 응답 관련 에러 (3200번대) ====================
    RESPONSE_NOT_FOUND("ERR_3201", "설문조사 응답을 찾을 수 없습니다.", 404),
    RESPONSE_DUPLICATE("ERR_3202", "이미 응답을 제출한 응답자입니다.", 409),
    RESPONSE_REQUIRED_MISSING("ERR_3203", "필수 질문에 대한 응답이 누락되었습니다.", 400),
    RESPONSE_INVALID_CHOICE("ERR_3204", "유효하지 않은 선택지입니다.", 400),
    RESPONSE_SUBMISSION_FAILED("ERR_3205", "응답 제출에 실패했습니다.", 500),
    RESPONSE_EMPTY_ANSWER("ERR_3206", "응답 내용이 비어있습니다.", 400),
    RESPONSE_TOO_MANY_CHOICES("ERR_3207", "허용된 선택지 개수를 초과했습니다.", 400),
    
    // ==================== 데이터베이스 관련 에러 (4000번대) ====================
    DATABASE_ERROR("ERR_4001", "데이터베이스 오류가 발생했습니다.", 500),
    TRANSACTION_FAILED("ERR_4002", "트랜잭션 처리에 실패했습니다.", 500),
    CONNECTION_TIMEOUT("ERR_4003", "데이터베이스 연결 시간이 초과되었습니다.", 408),
    
    // ==================== 외부 연동 관련 에러 (5000번대) ====================
    EXTERNAL_API_ERROR("ERR_5001", "외부 API 호출에 실패했습니다.", 502),
    EXTERNAL_API_TIMEOUT("ERR_5002", "외부 API 응답 시간이 초과되었습니다.", 504),
    EXTERNAL_SERVICE_UNAVAILABLE("ERR_5003", "외부 서비스를 사용할 수 없습니다.", 503);

    private final String code;
    private final String message;
    private final int httpStatusCode;

}
