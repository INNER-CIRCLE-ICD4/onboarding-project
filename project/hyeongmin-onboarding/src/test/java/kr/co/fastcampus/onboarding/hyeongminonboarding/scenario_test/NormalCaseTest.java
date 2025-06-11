package kr.co.fastcampus.onboarding.hyeongminonboarding.scenario_test;


import com.mysema.commons.lang.Assert;
import kr.co.fastcampus.onboarding.hyeongminonboarding.domain.repository.SurveyRepository;
import kr.co.fastcampus.onboarding.hyeongminonboarding.domain.entity.Survey;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

//@Disabled("Integration test")
//@DataJpaTest
@SpringBootTest
@Transactional
public class NormalCaseTest {

    @Autowired
    private SurveyRepository surveyRepository;

    @Test
    @Transactional
    public void easyCreateTest() {
        Survey survey = new Survey();
        survey.setName("Original");
        survey.setDescription("Desc");
//        survey = surveyRepository.save(survey);
        survey = surveyRepository.saveAndFlush(survey);
        System.out.println(survey.getId());


    }

    @Test
    public void testConcurrentUpdateWithQueryDsl() throws InterruptedException {
        // 초기 엔티티 저장
        Survey survey = new Survey();
        survey.setName("Original");
        survey.setDescription("Desc");
        survey = surveyRepository.save(survey);

        int threadCount = 2;
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        // thread A: QueryDsl update
        Survey finalSurvey = survey;
        executor.submit(() -> {
            surveyRepository.updateNameById(finalSurvey.getId(), "Thread1");
            latch.countDown();
        });

        // thread B: QueryDsl update
        Survey finalSurvey1 = survey;
        executor.submit(() -> {
            surveyRepository.updateNameById(finalSurvey1.getId(), "Thread2");
            latch.countDown();
        });

        latch.await();
        executor.shutdown();

        // 마지막 상태를 QueryDsl로 조회
        Survey result = surveyRepository.findByIdQueryDsl(survey.getId()).orElseThrow();
        System.out.println("Final name: " + result.getName());
    }

}
