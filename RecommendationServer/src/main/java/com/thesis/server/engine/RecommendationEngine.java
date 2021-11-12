package com.thesis.server.engine;

import com.thesis.server.repository.Neo4jRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RecommendationEngine {
    private final Neo4jRepository repository;

    @Autowired
    public RecommendationEngine(Neo4jRepository repository) {
        this.repository = repository;
    }

    public List<String> getRecommendation(List<String> category) {
        return repository.getResortsNames(category);
    }
}
