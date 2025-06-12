package fastcampus.onboarding.form.controller;

import fastcampus.onboarding.common.response.ApiResponse;
import fastcampus.onboarding.form.dto.request.FormCreateRequestDto;
import fastcampus.onboarding.form.service.FormCreateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api")
public class FormCreateController {
    private final FormCreateService formCreateService;

//    @PostMapping("/form")
//    public ApiResponse<FormResponseDto> createForm(@RequestBody FormCreateRequestDto formCreateRequestDto){
//
//    }


}
