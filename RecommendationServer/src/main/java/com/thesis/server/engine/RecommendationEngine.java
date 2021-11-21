package com.thesis.server.engine;

import com.thesis.server.repository.Neo4jRepository;
import com.thesis.server.repository.ResortDetails;
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

    public Map<String, ResortDetails> getResorts(){
        return repository.getResorts();
    }

    public Map<String, ResortDetails> getSimilarityRecommendation(String name) {
        String query = "MATCH (r:Resort {name: \"" + name + "\"})-[:ACCOMMODATION|TRANSFER|WINE_AND_DINE]-(t)-[:ACCOMMODATION|TRANSFER|WINE_AND_DINE]-(other:Resort)\n" +
                "WITH r, other, COUNT(t) AS intersection, COLLECT(t.type) AS i\n" +
                "MATCH (r)-[:ACCOMMODATION|TRANSFER|WINE_AND_DINE]-(m1)\n" +
                "WITH r, other, intersection, i, COLLECT(m1.type) AS s1\n" +
                "MATCH (other)-[:ACCOMMODATION|TRANSFER|WINE_AND_DINE]-(m2)\n" +
                "WITH r, other, intersection, i, s1, COLLECT(m2.type) AS s2\n" +
                "WITH r, other, intersection, i, s1, s2\n" +
                "WITH r, other, intersection, s1+[x IN s2 WHERE NOT x IN s1] AS union, s1, s2\n" +
                "RETURN other, ((1.0*intersection)/SIZE(union)) AS jaccard_score ORDER BY jaccard_score DESC";
        return repository.similarityRecommendation(query);
    }

    public Map<String, String> getSurveyRecommendation(SurveyAnswers answers){
        String starRatingOptions = buildOptions(answers.getStarRating().getOptions(),"");
        Double starRatingFactor = answers.getStarRating().getImportance() / answers.getStarRating().getOptions().size();
        String transferOptions = buildOptions(answers.getTransfer().getOptions(), "'");
        Double transferOptionsFactor = answers.getTransfer().getImportance() / answers.getTransfer().getOptions().size();
        String transferPrice = answers.getTransferPrice().getPrice().toString();
        String transferPriceImportance = answers.getTransferPrice().getImportance().toString();
        String transferTime = answers.getTransferTime().getTime().toString();
        String transferTimeImportance = answers.getTransferTime().getImportance().toString();
        String accommodationOptions = buildOptions(answers.getAccommodation().getOptions(), "'");
        Double accommodationFactor = answers.getAccommodation().getImportance() / answers.getAccommodation().getOptions().size();
        String accommodationPrice = answers.getAccommodationPrice().getPrice().toString();
        String accommodationPriceImportance = answers.getAccommodationPrice().getImportance().toString();
        StringBuilder boardOptions = new StringBuilder();
        String prefix = "";
        for(String s: answers.getBoardBasis().getOptions()){
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

        String query = String.format(
                "MATCH (r:Resort)-[:RATING]-(sr:StarRating)\n" +
                "WHERE sr.rating in %s\n" +
                "WITH r, count(sr) * %s as c\n" +
                "WITH COLLECT({name: r.name, score: c}) as resorts\n" +
                "\n" +
                "MATCH (r:Resort)-[:TRANSFER]-(tr:Transfer)\n" +
                "WHERE tr.type in %s\n" +
                "WITH resorts, r, count(tr) * %s as c\n" +
                "WITH resorts + COLLECT({name: r.name, score: c}) as resorts\n" +
                "\n" +
                "MATCH (r:Resort)-[t:TRANSFER]-(:Transfer)\n" +
                "WHERE t.time < %s\n" +
                "WITH DISTINCT r as r, resorts, %s as c\n" +
                "WITH resorts + COLLECT({name: r.name, score: c}) as resorts\n" +
                "\n" +
                "MATCH (r:Resort)-[t:TRANSFER]-(:Transfer)\n" +
                "WHERE t.price < %s\n" +
                "WITH DISTINCT r as r, resorts, %s as c\n" +
                "WITH resorts + COLLECT({name: r.name, score: c}) as resorts\n" +
                "\n" +
                "MATCH (r:Resort)-[:ACCOMMODATION]-(ac:Accommodation)\n" +
                "WHERE ac.type in %s \n" +
                "WITH r, resorts, count(ac) * %s as c\n" +
                "WITH resorts + COLLECT({name: r.name, score: c}) as resorts\n" +
                "\n" +
                "MATCH (r:Resort)-[a:ACCOMMODATION]-(:Accommodation)\n" +
                "WHERE a.halfBoard < %s OR a.fullBoard < %s OR a.allInclusive < %s\n" +
                "WITH DISTINCT r as r, resorts, %s as c\n" +
                "WITH resorts + COLLECT({name: r.name, score: c}) as resorts\n" +
                "\n" +
                "MATCH (r:Resort)-[a:ACCOMMODATION]-(:Accommodation)\n" +
                "WHERE %s \n" +
                "WITH DISTINCT r as r, resorts, %s as c\n" +
                "WITH resorts + COLLECT({name: r.name, score: c}) as resorts\n" +
                "\n" +
                "MATCH (r:Resort)-[:WATER_SPORTS]-(w:WaterSports)\n" +
                "WHERE w.name in %s \n" +
                "WITH r, resorts, count(w) * %s as c\n" +
                "WITH resorts + COLLECT({name: r.name, score: c}) as resorts\n" +
                "\n" +
                "MATCH (r:Resort)-[:WINE_AND_DINE]-(w:WineAndDine)\n" +
                "WHERE w.type in %s \n" +
                "WITH r, resorts, count(w) * %s as c\n" +
                "WITH resorts + COLLECT({name: r.name, score: c}) as resorts\n" +
                "\n" +
                "MATCH (r:Resort)-[:FITNESS]-(f:Fitness)\n" +
                "WHERE f.name in %s\n" +
                "WITH r, resorts, count(f) * %s as c\n" +
                "WITH resorts + COLLECT({name: r.name, score: c}) as resorts\n" +
                "\n" +
                "UNWIND resorts as resort\n" +
                "RETURN resort.name as resort, sum(resort.score) / %s * 100 as score ORDER BY score DESC",
                starRatingOptions, starRatingFactor,
                transferOptions, transferOptionsFactor,
                transferTime, transferTimeImportance,
                transferPrice, transferPriceImportance,
                accommodationOptions, accommodationFactor,
                accommodationPrice, accommodationPrice, accommodationPrice, accommodationPriceImportance,
                boardOptions, boardImportance,
                waterSportsOptions, waterSportsFactor,
                wineAndDineOptions, wineAndDineFactor,
                fitnessOptions, fitnessFactor,
                importanceSum);

//        System.out.println(query);
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
