package com.multi.sungwoongonboarding.questions.infrastructure;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.HashSet;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@Sql(scripts = "/sql/form_insert_data.sql")
public class QuestionJpaRepositoryTest {

    @Autowired
    QuestionJpaRepository questionJpaRepository;

    @Test
    @DisplayName("설문지에 해당하는 필수 질문 조회")
    public void findRequiredQuestions() {

        List<QuestionJpaEntity> requiredByFormId = questionJpaRepository.findRequiredByFormId(1L);

        //Then
        assertThat(requiredByFormId).isNotEmpty();
        assertThat(requiredByFormId).hasSize(2);
    }

    @Test
    @DisplayName("설문지 필수 질문에 대한 요청이 잘 왔는지 확인하기")
    public void allRequiredAnswered() {
        //Given
        List<QuestionJpaEntity> requiredByFormId = questionJpaRepository.findRequiredByFormId(1L);

        HashSet<Long> longs = new HashSet<>();
        longs.add(1L);
        longs.add(2L);
        longs.add(3L);


        boolean b = requiredByFormId.stream().map(QuestionJpaEntity::getId).allMatch(longs::contains);
        assertThat(b).isTrue();
    }

}