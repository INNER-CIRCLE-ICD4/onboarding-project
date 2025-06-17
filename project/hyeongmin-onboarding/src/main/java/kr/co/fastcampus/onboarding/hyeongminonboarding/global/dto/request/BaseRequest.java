package kr.co.fastcampus.onboarding.hyeongminonboarding.global.dto.request;


import jakarta.validation.Valid;
import com.fasterxml.jackson.annotation.JsonIgnore;
import kr.co.fastcampus.onboarding.hyeongminonboarding.domain.api.survey.dto.request.SubmitSurveyAnswersRequest;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@Data
public class BaseRequest<T> {
    @JsonIgnore
    private LocalDateTime requestTime;
    @Valid
    private T body;

    public BaseRequest() {
        this.requestTime = LocalDateTime.now();
    }

    public BaseRequest(T body) {
        this.requestTime = LocalDateTime.now();
        this.body = body;
    }

    public static <T> BaseRequest<T> of(T body) {
        return new BaseRequest<>(body);
    }
}
