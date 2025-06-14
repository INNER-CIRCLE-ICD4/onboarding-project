package com.INNER_CIRCLE_ICD4.innerCircle.controller;

import com.INNER_CIRCLE_ICD4.innerCircle.dto.ResponseRequest;
import com.INNER_CIRCLE_ICD4.innerCircle.dto.ResponseDto;
import com.INNER_CIRCLE_ICD4.innerCircle.service.ResponseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/responses")
@RequiredArgsConstructor
public class ResponseController {

    private final ResponseService responseService;

    @PostMapping
    public ResponseEntity<Void> submitResponse(
            @Valid @RequestBody ResponseRequest request
    ) {
        UUID responseId = responseService.saveResponse(request);
        URI location = URI.create("/responses/" + responseId);
        return ResponseEntity.created(location).build();
    }

    @GetMapping
    public ResponseEntity<List<ResponseDto>> getAllResponses() {
        List<ResponseDto> dtos = responseService.findAll();
        return ResponseEntity.ok(dtos);
    }
}