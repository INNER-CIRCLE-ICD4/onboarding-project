package fastcampus.onboarding.form.service;

import fastcampus.onboarding.form.dto.request.FormCreateRequestDto;
import fastcampus.onboarding.form.dto.response.FormCreateResponseDto;
import fastcampus.onboarding.form.repository.FormRespository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FormCreateService {
    private final FormRespository formRespository;
//
//    public FormCreateResponseDto createForm(FormCreateRequestDto requestDto) {
//    }
}
