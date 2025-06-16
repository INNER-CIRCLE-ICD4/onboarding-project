package fastcampus.onboarding.form.controller;

import fastcampus.onboarding.common.response.ApiResponse;
import fastcampus.onboarding.form.dto.request.FormCreateRequestDto;
import fastcampus.onboarding.form.dto.request.FormUpdateRequestDto;
import fastcampus.onboarding.form.service.FormService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@Slf4j
public class FormController {
    private final FormService formService;

    @PostMapping("/form")
    public ResponseEntity<ApiResponse> createForm(@Valid @RequestBody FormCreateRequestDto formCreateRequestDto) {
        formService.createForm(formCreateRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(HttpStatus.CREATED, "설문조사 생성을 성공하였습니다."));
    }

    @PutMapping("/form/{formSeq}")
    public ResponseEntity<ApiResponse> updateForm(@PathVariable Long formSeq, @RequestBody FormUpdateRequestDto formUpdateRequestDto) {
        formService.updateForm(formSeq, formUpdateRequestDto);
        return ResponseEntity.status(HttpStatus.OK)
                .body(ApiResponse.success(HttpStatus.OK, "설문조사를 수정하였습니다."));
    }
}
