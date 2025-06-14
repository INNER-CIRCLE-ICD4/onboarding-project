package fastcampus.onboarding.form.controller;

import fastcampus.onboarding.common.response.ApiResponse;
import fastcampus.onboarding.form.dto.request.FormCreateRequestDto;
import fastcampus.onboarding.form.service.FormService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
public class FormController {
    private final FormService formService;

    @PostMapping("/form")
    public ResponseEntity<ApiResponse> form(@RequestBody FormCreateRequestDto formCreateRequestDto) {

        formService.createForm(formCreateRequestDto);

        return ResponseEntity.ok(ApiResponse.success(HttpStatus.CREATED, "설문조사 생성을 성공하였습니다."));
    }
}
