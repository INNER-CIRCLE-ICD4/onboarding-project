package com.multi.sungwoongonboarding.responses.application;

import com.multi.sungwoongonboarding.responses.domain.Responses;
import com.multi.sungwoongonboarding.responses.application.repository.ResponseRepository;
import com.multi.sungwoongonboarding.responses.dto.ResponseCreateRequest;
import com.multi.sungwoongonboarding.responses.dto.ResponseSheet;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;



@Service
@RequiredArgsConstructor
public class ResponseService {

    private final ResponseRepository responseRepository;

    @Transactional
    public ResponseSheet submitResponse(ResponseCreateRequest request) {

        // 응답 저장
        Responses response = request.toDomain();
        Responses result = responseRepository.save(response);
        return ResponseSheet.fromDomain(result);
    }
}
