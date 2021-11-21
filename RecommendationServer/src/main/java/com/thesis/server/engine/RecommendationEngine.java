package com.thesis.server.engine;

import com.thesis.server.repository.Neo4jRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class RecommendationEngine {
    private final Neo4jRepository repository;

    @Autowired
    public RecommendationEngine(Neo4jRepository repository) {
        this.repository = repository;
    }

    public List<String> getRecommendation(SurveyAnswers answers) {
        String query = repository.buildQueryForResorts(answers.getStarRating().getOptions(), "StarRating", "rating");
        return repository.getResorts(query);
    }

//    private Map<String, Double> surveyWeighted(){
//
//    }

    public Map<String, String> test(SurveyAnswers answers){
        return repository.test(answers);
    }
}
