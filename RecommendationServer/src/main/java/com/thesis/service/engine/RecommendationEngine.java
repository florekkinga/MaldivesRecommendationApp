package com.thesis.service.engine;

import com.thesis.service.repository.Neo4jRepository;
import com.thesis.service.controller.response.Resort;
import com.thesis.service.controller.response.ResortFacilities;
import com.thesis.service.answers.SurveyAnswers;
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

    public Map<String, Resort> getResorts() {
        return repository.getResorts();
    }

    public ResortFacilities getResortDetailsAndFacilities(String name) {
        return repository.getResortDetailsAndFacilities(name);
    }

    public Map<String, Resort> getSimilarityRecommendation(String name) {

        String query = "MATCH (r:Resort {name: \"" + name + "\"})--(t)--(o:Resort)\n" +
                "WITH r, o, COUNT(t) AS intersection\n" +
                "\n" +
                "MATCH (r)--(node1)\n" +
                "WITH o, intersection, COLLECT(ID(node1)) AS set1\n" +
                "MATCH (o)--(node2)\n" +
                "WITH o, intersection, set1, COLLECT(ID(node2)) AS set2\n" +
                "WITH o, intersection, set1, set2\n" +
                "WITH o, intersection, set1 + [x IN set2 WHERE NOT x IN set1] AS union\n" +
                "WITH o, ((1.0*intersection)/SIZE(union)) AS jaccard_score\n" +
                "\n" +
                "RETURN o AS other, jaccard_score \n" +
                "ORDER BY jaccard_score DESC";

        System.out.println(query);
        return repository.similarityRecommendation(query);
    }

    public Map<String, Resort> getSurveyRecommendation(SurveyAnswers answers) {
        String starRatingOptions = buildOptions(answers.getStarRating().getOptions(), "");
        Double starRatingFactor = answers.getStarRating().getImportance();
        String transferOptions = buildOptions(answers.getTransfer().getOptions(), "'");
        Double transferOptionsFactor = answers.getTransfer().getImportance();
        String transferPrice = answers.getTransferPrice().getPrice().toString();
        String transferPriceImportance = answers.getTransferPrice().getImportance().toString();
        String transferTime = answers.getTransferTime().getTime().toString();
        String transferTimeImportance = answers.getTransferTime().getImportance().toString();
        String accommodationOptions = buildOptions(answers.getAccommodation().getOptions(), "'");
        Double accommodationFactor = answers.getAccommodation().getImportance();
        String accommodationPrice = answers.getAccommodationPrice().getPrice().toString();
        String accommodationPriceImportance = answers.getAccommodationPrice().getImportance().toString();
        StringBuilder boardOptions = new StringBuilder();
        String prefix = "";
        for (String s : answers.getBoardBasis().getOptions()) {
            boardOptions.append(prefix);
            prefix = "OR ";
            boardOptions.append("a.").append(s).append(" IS NOT NULL ");
        }
        String boardImportance = answers.getBoardBasis().getImportance().toString();
        String waterSportsOptions = buildOptions(answers.getWaterSports().getOptions(), "'");
        Double waterSportsFactor = answers.getWaterSports().getImportance() / answers.getWaterSports().getOptions().size();
        String wineAndDineOptions = buildOptions(answers.getWineAndDine().getOptions(), "'");
        Double wineAndDineFactor = answers.getWineAndDine().getImportance() / answers.getWineAndDine().getOptions().size();
        String fitnessOptions = buildOptions(answers.getFitness().getOptions(), "'");
        Double fitnessFactor = answers.getFitness().getImportance() / answers.getFitness().getOptions().size();

        Double importanceSum = answers.getSumOfImportance();
        Boolean empty = true;
        String query = "MATCH (r:Resort)\n" +
                "WITH COLLECT({name: r, score: 0}) as resorts\n";

        if (!answers.getStarRating().getOptions().isEmpty()) {
            empty = false;
            query += String.format(
                    "MATCH (r:Resort)-[:RATING]-(sr:StarRating)\n" +
                            "WHERE sr.rating in %s\n" +
                            "WITH DISTINCT r as r, resorts, %s as c\n" +
                            "WITH resorts + COLLECT({name: r, score: c}) as resorts\n",
                    starRatingOptions, starRatingFactor);
        }
        if (!answers.getTransfer().getOptions().isEmpty()) {
            empty = false;
            query += String.format(
                    "MATCH (r:Resort)-[:TRANSFER]-(tr:Transfer)\n" +
                            "WHERE tr.type in %s\n" +
                            "WITH DISTINCT r as r, resorts, %s as c\n" +
                            "WITH resorts + COLLECT({name: r, score: c}) as resorts\n",
                    transferOptions, transferOptionsFactor);
        }
        if (answers.getTransferTime().getTime() != 0) {
            empty = false;
            query += String.format(
                    "MATCH (r:Resort)-[t:TRANSFER]-(:Transfer)\n" +
                            "WHERE t.time <= %s\n" +
                            "WITH DISTINCT r as r, resorts, %s as c\n" +
                            "WITH resorts + COLLECT({name: r, score: c}) as resorts\n",
                    transferTime, transferTimeImportance);
        }
        if (answers.getTransferPrice().getPrice() != 0) {
            empty = false;
            query += String.format(
                    "MATCH (r:Resort)-[t:TRANSFER]-(:Transfer)\n" +
                            "WHERE t.price <= %s\n" +
                            "WITH DISTINCT r as r, resorts, %s as c\n" +
                            "WITH resorts + COLLECT({name: r, score: c}) as resorts\n",
                    transferPrice, transferPriceImportance);
        }
        if (!answers.getAccommodation().getOptions().isEmpty()) {
            empty = false;
            query += String.format(
                    "MATCH (r:Resort)-[:ACCOMMODATION]-(ac:Accommodation)\n" +
                            "WHERE ac.type in %s \n" +
                            "WITH DISTINCT r as r, resorts, %s as c\n" +
                            "WITH resorts + COLLECT({name: r, score: c}) as resorts\n",
                    accommodationOptions, accommodationFactor);
        }
        if (answers.getAccommodationPrice().getPrice() != 0) {
            empty = false;
            query += String.format(
                    "MATCH (r:Resort)-[a:ACCOMMODATION]-(:Accommodation)\n" +
                            "WHERE a.halfBoard <= %s OR a.fullBoard <= %s OR a.allInclusive <= %s\n" +
                            "WITH DISTINCT r as r, resorts, %s as c\n" +
                            "WITH resorts + COLLECT({name: r, score: c}) as resorts\n",
                    accommodationPrice, accommodationPrice, accommodationPrice, accommodationPriceImportance);
        }
        if (!answers.getBoardBasis().getOptions().isEmpty()) {
            empty = false;
            query += String.format(
                    "MATCH (r:Resort)-[a:ACCOMMODATION]-(:Accommodation)\n" +
                            "WHERE %s \n" +
                            "WITH DISTINCT r as r, resorts, %s as c\n" +
                            "WITH resorts + COLLECT({name: r, score: c}) as resorts\n",
                    boardOptions, boardImportance);
        }
        if (!answers.getWaterSports().getOptions().isEmpty()) {
            empty = false;
            query += String.format(
                    "MATCH (r:Resort)-[:WATER_SPORTS]-(w:WaterSports)\n" +
                            "WHERE w.name in %s \n" +
                            "WITH r, resorts, count(w) * %s as c\n" +
                            "WITH resorts + COLLECT({name: r, score: c}) as resorts\n",
                    waterSportsOptions, waterSportsFactor);
        }
        if (!answers.getWineAndDine().getOptions().isEmpty()) {
            empty = false;
            query += String.format(
                    "MATCH (r:Resort)-[:WINE_AND_DINE]-(w:WineAndDine)\n" +
                            "WHERE w.type in %s \n" +
                            "WITH r, resorts, count(w) * %s as c\n" +
                            "WITH resorts + COLLECT({name: r, score: c}) as resorts\n",
                    wineAndDineOptions, wineAndDineFactor);
        }
        if (!answers.getFitness().getOptions().isEmpty()) {
            empty = false;
            query += String.format(
                    "MATCH (r:Resort)-[:FITNESS]-(f:Fitness)\n" +
                            "WHERE f.name in %s\n" +
                            "WITH r, resorts, count(f) * %s as c\n" +
                            "WITH resorts + COLLECT({name: r, score: c}) as resorts\n",
                    fitnessOptions, fitnessFactor);
        }

        if(importanceSum != 0 && !empty){
            query += String.format("UNWIND resorts as resort\n" +
                    "RETURN resort.name as r, sum(resort.score) / %s * 100 as score ORDER BY score DESC",
                    importanceSum);
            System.out.println(query);
        }
        else{
            query = "MATCH (r:Resort) RETURN r, 100 as score";
        }
        return repository.surveyRecommendation(query);
    }

    private String buildOptions(List<String> options, String quote) {
        String prefix = "";
        StringBuilder stringOptions = new StringBuilder("[");
        for (String c : options) {
            stringOptions.append(prefix);
            prefix = ",";
            stringOptions.append(quote).append(c).append(quote);
        }
        stringOptions.append("]");
        return stringOptions.toString();
    }
}
