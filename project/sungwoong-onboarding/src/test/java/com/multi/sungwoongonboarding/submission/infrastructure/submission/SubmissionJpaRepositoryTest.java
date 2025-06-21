package com.multi.sungwoongonboarding.submission.infrastructure.submission;

import com.multi.sungwoongonboarding.submission.domain.Answers;
import com.multi.sungwoongonboarding.submission.domain.SelectedOption;
import com.multi.sungwoongonboarding.submission.domain.Submission;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@EnableJpaAuditing
@Sql(scripts = "/sql/submission_insert_data.sql")
class SubmissionJpaRepositoryTest {

    @Autowired
    SubmissionJpaRepository submissionJpaRepository;

    @Autowired
    EntityManager entityManager;


    @Test
    @DisplayName("Submission :  Jpa 저장")
    public void jpa_저장() {
        //Given
        Answers answers = Answers.builder()
                .questionId(1L)
                .selectedOptions(List.of(
                        SelectedOption.builder()
                                .optionId(1L)
                                .build(),
                        SelectedOption.builder()
                                .optionId(2L)
                                .build(),
                        SelectedOption.builder()
                                .optionId(3L)
                                .build()))
                .build();
        Submission submission = Submission.builder()
                .formId(1L)
                .userId("sungwoong222")
                .answers(List.of(answers))
                .build();

        SubmissionJpaEntity submissionJpaEntity = SubmissionJpaEntity.fromDomainWithFormVersion(submission, 1);


        //When
        SubmissionJpaEntity save = submissionJpaRepository.save(submissionJpaEntity);

        entityManager.flush();
        entityManager.clear();

        List<SubmissionJpaEntity> byFormId = submissionJpaRepository.findByFormId(save.getId(), null, null);

        //Then
        assertThat(byFormId).isNotNull();
        assertThat(byFormId).hasSize(4);
        assertThat(byFormId.get(0).getAnswers()).hasSize(3);
        assertThat(byFormId.get(0).getAnswers().get(2).getOptionId()).isEqualTo(3L);
    }

    @Test
    @DisplayName("Submission : JPA 조회")
    public void jpa_조회하기() {

        //When
        List<SubmissionJpaEntity> all = submissionJpaRepository.findAll();

        //Then
        assertThat(all).isNotNull();
        assertThat(all).hasSize(3);
    }

    @Test
    @DisplayName("jpql_test")
    public void jpql_test() {


        String query = """
                select count(q)
                from QuestionJpaEntity q
                where q.formsJpaEntity.id = 1
                and q.questionText like concat('%', '비', '%')
                """;

        List<Long> resultList = entityManager.createQuery(query, Long.class).getResultList();

        assertThat(resultList).isNotNull();
        assertThat(resultList).hasSize(1);

    }

    @Test
    @DisplayName("jpql_test2")
    public void jpql_test2() {


        String query = """
                select a.submissionJpaEntity.id 
                from AnswerJpaEntity a 
                left join OptionsJpaEntity o on o.id = a.optionId
                where a.submissionJpaEntity.id = 12  
                and (:answerText is null or a.answerText like CONCAT('%', :answerText, '%')) 
                """;

        List<Long> resultList = entityManager.createQuery(query, Long.class).setParameter("answerText", "마").getResultList();

        assertThat(resultList).isNotNull();
        assertThat(resultList).hasSize(1);

    }

    @Test
    @DisplayName("jpql_test3")
    public void jpql_test3() {

        String query = """
                select s
                from SubmissionJpaEntity s
                left join FormsJpaEntity f on s.formId = f.id
                where s.formId = 1
                and :questionText is null or exists (
                        select q
                        from AnswerJpaEntity a 
                        left join QuestionJpaEntity q on a.questionId = q.id 
                        where a.submissionJpaEntity.id = s.id 
                        and q.questionText like concat('%', :questionText, '%')
                )
                """;

        List<SubmissionJpaEntity> resultList = entityManager.createQuery(query, SubmissionJpaEntity.class)
                .setParameter("questionText", "이유")
                .getResultList();

        assertThat(resultList).isNotNull();
        assertThat(resultList).hasSize(2);
    }


    @Test
    @DisplayName("jpql_test4")
    public void jpql_test4() {

        String query = """
                select s
                from SubmissionJpaEntity s
                left join FormsJpaEntity f on s.formId = f.id
                where s.formId = 1
                and :answerText is null or exists (
                        select a.id
                        from AnswerJpaEntity a 
                        left join OptionsJpaEntity o on a.optionId = o.id
                        where a.submissionJpaEntity.id = s.id
                        and (a.answerText like concat('%', :answerText, '%') or o.optionText like concat('%', :answerText, '%'))
                )
                """;

        List<SubmissionJpaEntity> resultList = entityManager.createQuery(query, SubmissionJpaEntity.class)
                .setParameter("answerText", "마마")
                .getResultList();

        assertThat(resultList).isNotNull();
        assertThat(resultList).hasSize(1);
    }

    @Test
    @DisplayName("jpql_test5")
    public void jpql_test6() {

        String query = """
                select s
                from SubmissionJpaEntity s
                left join FormsJpaEntity f on s.formId = f.id
                where s.formId = :formId
                and (:questionText is null or 
                            exists (
                                select sub_a.id 
                                from AnswerJpaEntity sub_a 
                                left join QuestionJpaEntity q on sub_a.questionId = q.id 
                                where sub_a.submissionJpaEntity.id = s.id 
                                and q.questionText like concat('%', :questionText, '%')
                )) 
                and (:answerText is null or 
                            exists (
                                select sub_a.id
                                from AnswerJpaEntity sub_a 
                                left join OptionsJpaEntity o on sub_a.optionId = o.id
                                where sub_a.submissionJpaEntity.id = s.id
                                and (sub_a.answerText like concat('%', :answerText, '%') or o.optionText like concat('%', :answerText, '%'))
                )) 
            """;

        List<SubmissionJpaEntity> resultList = entityManager.createQuery(query, SubmissionJpaEntity.class)
                .setParameter("formId", "1")
                .setParameter("questionText", null)
                .setParameter("answerText", "마마")
                .getResultList();

        assertThat(resultList).isNotNull();
        assertThat(resultList).hasSize(1);
    }
}