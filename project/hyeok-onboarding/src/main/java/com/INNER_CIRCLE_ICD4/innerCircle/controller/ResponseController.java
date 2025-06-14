package com.INNER_CIRCLE_ICD4.innerCircle.controller;

import com.INNER_CIRCLE_ICD4.innerCircle.dto.ResponseRequest;
import com.INNER_CIRCLE_ICD4.innerCircle.service.ResponseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/responses")
@RequiredArgsConstructor
public class ResponseController {

    private final ResponseService responseService;

    @PostMapping
    public ResponseEntity<UUID> submitResponse(@RequestBody ResponseRequest request) {
        UUID responseId = responseService.saveResponse(request);
        return ResponseEntity.ok(responseId);
    }
}