package kr.innercircle.onboarding.survey.aop

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
 * packageName : kr.innercircle.onboarding.survey.aop
 * fileName    : LoggerAspect
 * author      : ckr
 * date        : 25. 6. 12.
 * description :
 */

@Aspect
@Component
class LoggerAspect {
    private val logger = LoggerFactory.getLogger(this.javaClass)

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

    @Pointcut("within(kr.innercircle.onboarding.survey.controller..*)")
    fun onRequest() {
        println("----------------------------")
    }

    @Around("kr.innercircle.onboarding.survey.aop.LoggerAspect.onRequest()")
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
            pjp.proceed(pjp.args)
        } finally {
            val end = System.currentTimeMillis()
            logger.debug(
                "Request: {} {}{} < {} ({}ms)",
                request.method,
                request.requestURI,
                params,
                request.remoteHost,
                end - start
            )
        }
    }
}