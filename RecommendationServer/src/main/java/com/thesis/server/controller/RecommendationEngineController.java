package com.thesis.server.controller;

import com.thesis.server.engine.RecommendationEngine;
import com.thesis.server.engine.SurveyAnswers;
import com.thesis.server.repository.ResortDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
public class RecommendationEngineController {

    private final RecommendationEngine engine;

    @Autowired
    public RecommendationEngineController(RecommendationEngine engine) {
        this.engine = engine;
    }

    @PostMapping(path = "/recommendation", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, ResortDetails> getSurveyRecommendation(@RequestBody SurveyAnswers answers) {
        return engine.getSurveyRecommendation(answers);
    }

    @PostMapping(path = "/similarity/", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, ResortDetails> getSimilarityRecommendation(@RequestBody Map<String, String> resort) {
        return engine.getSimilarityRecommendation(resort.get("name"));
    }

    @GetMapping(path = "/resorts", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, ResortDetails> getAllResorts() {
        return engine.getResorts();
    }
}
