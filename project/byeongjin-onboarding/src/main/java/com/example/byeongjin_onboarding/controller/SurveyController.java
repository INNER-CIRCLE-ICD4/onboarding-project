package com.example.byeongjin_onboarding.controller;

import com.example.byeongjin_onboarding.dto.SurveyCreateResponse;
import com.example.byeongjin_onboarding.service.SurveyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.NoSuchElementException;

@Controller
@RequestMapping("/survey")
public class SurveyController {

    @Autowired
    private SurveyService surveyService;

    @GetMapping("/editor")
    public String showSurveyEditor(@RequestParam(value = "id", required = false) Long id, Model model) {
        if (id != null) {
            try {
                SurveyCreateResponse survey = surveyService.getSurveyById(id);
                model.addAttribute("surveyData", survey);
                model.addAttribute("pageTitle", survey.getName() + " 편집");
            } catch (NoSuchElementException e) {
                model.addAttribute("error", "해당 설문조사를 찾을 수 없습니다.");
                model.addAttribute("surveyData", new SurveyCreateResponse()); // 빈 객체라도 넣어 널포인트 방지
                model.addAttribute("pageTitle", "설문조사 없음");
            }
        } else {
            model.addAttribute("surveyData", new SurveyCreateResponse()); // 새 설문조사 생성 시 빈 DTO 전달
            model.addAttribute("pageTitle", "새 설문조사 생성");
        }
        return "survey-editor";
    }

    @GetMapping("/list")
    public String listSurveys(Model model) {
        List<SurveyCreateResponse> surveys = surveyService.getAllSurveys();
        model.addAttribute("surveys", surveys);
        model.addAttribute("pageTitle", "설문조사 목록");
        return "survey-list"; // templates/survey-list.html 렌더링
    }
}