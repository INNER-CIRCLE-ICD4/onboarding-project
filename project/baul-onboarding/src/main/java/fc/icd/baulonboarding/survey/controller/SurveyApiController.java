package fc.icd.baulonboarding.survey.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/v1/api/surveys")
public class SurveyApiController {

    @PostMapping("")
    public void 설문조사생성API(){

    }

    @PutMapping("")
    public void 설문조사수정API(){

    }

    @PostMapping("/submit")
    public void 설문조사응답제출API(){

    }

}
