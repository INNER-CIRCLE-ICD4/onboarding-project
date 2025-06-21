package com.example.byeongjin_onboarding.controller;

import com.example.byeongjin_onboarding.dto.SurveyAnswerSummaryResponse;
import com.example.byeongjin_onboarding.service.SurveyAnswerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.NoSuchElementException;

@Controller
@RequestMapping("/survey/answers")
public class SurveyAnswerController {

    @Autowired
    private SurveyAnswerService surveyAnswerService;

    @GetMapping("/results")
    public String showSurveyResults(@RequestParam("id") Long surveyId, Model model) {
        try {
            SurveyAnswerSummaryResponse summary = surveyAnswerService.getSurveyAnswerSummary(surveyId);
            model.addAttribute("surveySummary", summary);
            model.addAttribute("pageTitle", summary.getSurveyName() + " 응답 결과");
            return "survey-results";
        } catch (NoSuchElementException e) {
            model.addAttribute("error", "해당 설문조사에 대한 응답 결과를 찾을 수 없거나 데이터가 부족합니다.");
            model.addAttribute("pageTitle", "응답 결과 없음");
            return "survey-results";
        } catch (Exception e) {
            model.addAttribute("error", "응답 결과를 불러오는 중 오류가 발생했습니다: " + e.getMessage());
            model.addAttribute("pageTitle", "오류 발생");
            return "survey-results";
        }
    }
}