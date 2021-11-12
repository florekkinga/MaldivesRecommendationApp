package com.thesis.server.controller;

import com.thesis.server.engine.RecommendationEngine;
import com.thesis.server.engine.SurveyAnswers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RecommendationEngineController {

    private final RecommendationEngine engine;

    @Autowired
    public RecommendationEngineController(RecommendationEngine engine) {
        this.engine = engine;
    }

    @PostMapping(path = "/recommendation", consumes = "application/json", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public List<String> getResortsNames(@RequestBody SurveyAnswers answers) {
//        return engine.getRecommendation(category);
        System.out.println(answers.getStarRating().getImportance());
        System.out.println(answers.getStarRating().getOptions().toString());
        System.out.println(answers.getTransfer().getImportance());
        System.out.println(answers.getTransfer().getOptions().toString());
        return answers.getStarRating().getOptions();
    }
}
