package com.multi.sungwoongonboarding.submission.application;


import com.multi.sungwoongonboarding.submission.dto.SubmissionResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class SubmissionResponseTest {


    @Test
    @DisplayName("설문지에 해당하는 응답지를 조회한다")
    public void 응답지_조회() {

        //Given
        SubmissionService submissionService = SubmissionService.builder()
                .submissionRepository(new StubSubmissionRepository())
                .formRepository(new StubFormRepository())
                .build();


        //When
        Long 설문지_id = 1L;
        List<SubmissionResponse> submissionByFormId = submissionService.getSubmissionByFormId(설문지_id);


        //Then
        assertThat(submissionByFormId).hasSize(2);
        assertThat(submissionByFormId.get(0).getQuestionAnswer()).hasSize(2);
        assertThat(submissionByFormId.get(1).getQuestionAnswer()).hasSize(2);
        assertThat(submissionByFormId.get(1).getQuestionAnswer().get(0).getAnswerResponses()).hasSize(1);
        assertThat(submissionByFormId.get(1).getQuestionAnswer().get(0).getAnswerResponses().contains("햄버거는 완전식품에 맛있다.")).isTrue();
    }
}
