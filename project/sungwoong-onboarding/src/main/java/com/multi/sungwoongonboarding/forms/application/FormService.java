package com.multi.sungwoongonboarding.forms.application;

import com.multi.sungwoongonboarding.forms.application.repository.FormRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FormService {

    private final FormRepository formRepository;


}
