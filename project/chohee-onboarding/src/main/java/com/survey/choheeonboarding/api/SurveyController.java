package com.survey.choheeonboarding.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SurveyController {

    @GetMapping("")
    public String getSurvey() {
        return "survey";
    }
}
