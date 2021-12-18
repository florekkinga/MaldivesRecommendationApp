package com.thesis.service.controller;

import com.thesis.service.engine.RecommendationEngine;
import com.thesis.service.answers.SurveyAnswers;
import com.thesis.service.controller.response.Resort;
import com.thesis.service.controller.response.ResortFacilities;
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
    public Map<String, Resort> getSurveyRecommendation(@RequestBody SurveyAnswers answers) {
        return engine.getSurveyRecommendation(answers);
    }

    @PostMapping(path = "/similarity/", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, Resort> getSimilarityRecommendation(@RequestBody Map<String, String> resort) {
        return engine.getSimilarityRecommendation(resort.get("name"));
    }

    @PostMapping(path = "/resortDetails", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResortFacilities getResortDetails(@RequestBody Map<String, String> resort) {
        return engine.getResortDetailsAndFacilities(resort.get("name"));
    }

    @GetMapping(path = "/resorts", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, Resort> getAllResorts() {
        return engine.getResorts();
    }
}
