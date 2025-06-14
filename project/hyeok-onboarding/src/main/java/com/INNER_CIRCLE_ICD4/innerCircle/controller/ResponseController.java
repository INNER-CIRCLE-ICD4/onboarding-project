package com.INNER_CIRCLE_ICD4.innerCircle.controller;

import com.INNER_CIRCLE_ICD4.innerCircle.dto.*;
import com.INNER_CIRCLE_ICD4.innerCircle.service.ResponseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/responses")
@RequiredArgsConstructor
public class ResponseController {

    private final ResponseService responseService;

    @PostMapping
    public ResponseEntity<Void> submitResponse(
            @Valid @RequestBody ResponseRequest request
    ) {
        ResponseDto dto = responseService.saveResponse(request);
        URI location = URI.create("/responses/" + dto.id());
        return ResponseEntity.created(location).build();
    }

    @GetMapping
    public ResponseEntity<List<ResponseDto>> getAllResponses() {
        return ResponseEntity.ok(responseService.findAll());
    }
}