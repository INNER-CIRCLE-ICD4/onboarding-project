package fc.icd.baulonboarding.answer.controller;

import fc.icd.baulonboarding.answer.model.dto.AnswerCommand;
import fc.icd.baulonboarding.answer.model.dto.AnswerDto;
import fc.icd.baulonboarding.answer.model.dto.AnswerInfo;
import fc.icd.baulonboarding.answer.model.mapper.AnswerDtoMapper;
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
    private final AnswerDtoMapper answerDtoMapper;

    @PostMapping
    public CommonResponse registerAnswer(@RequestBody @Valid AnswerDto.RegisterAnswerRequest request){
        AnswerCommand.RegisterAnswer answerCommand = answerDtoMapper.of(request);
        answerService.registerAnswer(answerCommand);
        return CommonResponse.success("ok");
    }

    @GetMapping("/{answerId}")
    public CommonResponse retrieveAnswer(@PathVariable Long answerId){
        AnswerInfo.Answer result = answerService.retrieveAnswer(answerId);
        AnswerDto.Answer response = answerDtoMapper.of(result);
        return CommonResponse.success(response);
    }

}
