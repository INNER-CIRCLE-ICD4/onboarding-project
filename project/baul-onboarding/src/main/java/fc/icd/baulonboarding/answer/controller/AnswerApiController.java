package fc.icd.baulonboarding.answer.controller;

import fc.icd.baulonboarding.answer.model.dto.AnswerDto;
import fc.icd.baulonboarding.answer.service.AnswerService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/api/answers")
public class AnswerApiController {

    private final AnswerService answerService;

    @PostMapping
    public void registerAnswer(@RequestBody AnswerDto.RegisterAnswerRequest request){
        answerService.registerAnswer(request);
    }

    @GetMapping
    public void 설문조사응답조회API(){

    }

}
