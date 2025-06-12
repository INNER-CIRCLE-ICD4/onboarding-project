package fc.icd.baulonboarding.answer.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/api/answers")
public class AnswerApiController {

    @PostMapping
    public void 설문조사응답제출API(){

    }

    @GetMapping
    public void 설문조사응답조회API(){

    }

}
