package com.survey

import org.slf4j.LoggerFactory
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component
import org.springframework.jdbc.core.JdbcTemplate;

//@Component
class TableCheckRunner(private val jdbcTemplate: JdbcTemplate) {
    private val log = LoggerFactory.getLogger(TableCheckRunner::class.java)

    @EventListener(ApplicationReadyEvent::class)
    fun checkTable() {
        val tables = jdbcTemplate.queryForList("SELECT TABLE_NAME FROM INFORMATION_SCHEMA.TABLES WHERE TABLE_SCHEMA = 'PUBLIC'")
        String::class.java
        log.info("==== H2 DB에 생성된 테이블 목록 ====")
        tables.forEach { tableMap ->
            log.info("테이블: ${tableMap["TABLE_NAME"]}")
        }
        log.info("===================================")
    }
}