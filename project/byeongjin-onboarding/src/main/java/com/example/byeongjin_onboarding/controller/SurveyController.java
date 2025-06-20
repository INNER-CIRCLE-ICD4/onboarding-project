package com.example.byeongjin_onboarding.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/survey")
public class SurveyController {

    @GetMapping("/editor")
    public String showSurveyEditor(Model model) {
        model.addAttribute("pageTitle", "새 설문조사 생성");
        return "survey-editor";
    }
}