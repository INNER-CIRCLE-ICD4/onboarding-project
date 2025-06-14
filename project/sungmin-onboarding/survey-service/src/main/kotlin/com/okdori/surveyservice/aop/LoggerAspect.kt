package com.okdori.surveyservice.aop

import jakarta.servlet.http.HttpServletRequest
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.aspectj.lang.annotation.Pointcut
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import java.util.stream.Collectors

/**
 * author       : okdori
 * date         : 2025. 6. 10.
 * description  :
 */

inline fun <reified T> T.logger() = LoggerFactory.getLogger(T::class.java)!!

@Component
@Aspect
class LoggerAspect {
    /**
     * 요청 파라미터 맵을 문자열로 변환하는 메서드
     * 
     * @param paramMap 요청 파라미터 맵
     * @return 파라미터 맵을 문자열로 변환한 결과
     */
    private fun paramMapToString(paramMap: Map<String, Array<String>>): String {
        return paramMap.entries.stream()
            .map {
                String.format(
                    "%s -> (%s)",
                    it.key,
                    it.value.joinToString()
                )
            }
            .collect(Collectors.joining(", "))
    }

    /**
     * 컨트롤러 패키지 내의 모든 메서드를 대상으로 하는 포인트컷 정의
     */
    @Pointcut("within(com.okdori.surveyservice.controller..*)")
    fun onRequest() {
        println("----------------------------")
    }

    /**
     * 컨트롤러 요청에 대한 로깅을 수행하는 어드바이스
     * 
     * @param pjp 진행 중인 조인 포인트
     * @return 원본 메서드의 실행 결과
     * @throws Throwable 원본 메서드에서 발생할 수 있는 예외
     */
    @Around("com.okdori.surveyservice.aop.LoggerAspect.onRequest()")
    @Throws(Throwable::class)
    fun doLogging(pjp: ProceedingJoinPoint): Any? {
        val request: HttpServletRequest = (RequestContextHolder.currentRequestAttributes() as ServletRequestAttributes).request
        val paramMap: Map<String, Array<String>> = request.parameterMap
        var params = ""
        if (paramMap.isNotEmpty()) {
            params = " [" + paramMapToString(paramMap) + "]"
        }

        val start = System.currentTimeMillis()

        return try {
            pjp.proceed(pjp.args) // 원본 메서드 실행
        } finally {
            val end = System.currentTimeMillis()
            logger.debug(
                // 요청 메서드, URI, 파라미터, 요청자 IP, 처리 시간 로깅
                "Request: {} {}{} < {} ({}ms)",
                request.method,
                request.requestURI,
                params,
                request.remoteHost,
                end - start
            )
        }
    }

    companion object {
        private val logger = logger()
    }
}
