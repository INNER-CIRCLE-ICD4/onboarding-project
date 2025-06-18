package fc.icd.baulonboarding.answer.controller;

import fc.icd.baulonboarding.answer.model.dto.AnswerDto;
import fc.icd.baulonboarding.answer.model.dto.AnswerInfo;
import fc.icd.baulonboarding.answer.service.AnswerService;
import fc.icd.baulonboarding.common.reponse.CommonResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/api/answers")
public class AnswerApiController {

    private final AnswerService answerService;

    @PostMapping
    public void registerAnswer(@RequestBody @Valid AnswerDto.RegisterAnswerRequest request){
        answerService.registerAnswer(request);
    }

    @GetMapping("/{answerId}")
    public CommonResponse retrieveAnswer(@PathVariable Long answerId){
        AnswerInfo.Answer response = answerService.retrieveAnswer(answerId);
        return CommonResponse.success(response);
    }

}
