package kr.co.fastcampus.onboarding.hyeongminonboarding.domain.api.survey.controller;


import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@Profile({"local","dev"}) // prod 환경에 배포되지 않게 설정
@RestController
@RequestMapping("/api/mock_test")
public class MockTestController {
    @PostMapping
    public ResponseEntity<Void> create(@Valid @RequestBody SurveyMockRequest dto) {
        // 테스트용 stub; 아무것도 안 던지면 200 OK
        return ResponseEntity.ok().build();
    }

    @Data
    static class SurveyMockRequest {
        @NotBlank
        private String name;

        @NotNull @Size(min=1, max=10)
        private List<@NotNull Question> questions;

        @Data
        static class Question {
            @NotBlank
            private String title;
            @NotNull
            private String type;
        }
    }

}
