package com.choheeonboarding.survey;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SurveyController {

    @GetMapping("")
    public String getSurvey() {
        return "survey";
    }
}
