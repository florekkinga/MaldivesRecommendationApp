package com.thesis.server.repository;

import com.thesis.server.engine.SurveyAnswers;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class Neo4jRepository {

    private final Driver driver;
    private static final DecimalFormat df = new DecimalFormat("0.00");

    @Autowired
    public Neo4jRepository(Driver driver) {
        this.driver = driver;
    }

    public List<String> getResorts(String queryResorts) {

        try (Session session = driver.session()) {
            return session.run(queryResorts)
                    .list(r -> r.get("m").asNode().get("name").asString());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ArrayList<String>();
        }
    }

    public String buildQueryForResorts(List<String> options, String nodeType, String nodeAttribute) {
        return "MATCH (m:Resort)--(n:" +
                nodeType +
                ") WHERE n." +
                nodeAttribute +
                " IN " +
                buildOptions(options, "") +
                " RETURN m";
    }

    public String buildQueryForResorts(String option, String nodeType, String relationshipName, String relationshipAttribute) {
        return "MATCH (m:Resort)-[r:" +
                relationshipName +
                "]-(n:" +
                nodeType +
                ") WHERE r." +
                relationshipAttribute +
                " < " +
                option +
                " RETURN m";
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

    public Map<String, String> test(SurveyAnswers answers) {
        Map<String, String> resorts = new HashMap<>();
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

        Double importanceSum = answers.getStarRating().getImportance() +
                answers.getTransfer().getImportance() + answers.getTransferPrice().getImportance() +
                answers.getTransferTime().getImportance() + answers.getAccommodation().getImportance() +
                answers.getAccommodationPrice().getImportance() + answers.getBoardBasis().getImportance() +
                answers.getWaterSports().getImportance() + answers.getWineAndDine().getImportance() +
                answers.getFitness().getImportance();


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

        System.out.println(query);

        try (Session session = driver.session()) {
            Result result = session.run(query);
            List<Record> records = result.list();
            System.out.println(records);
            for (Record r : records) {
                String score = df.format(r.get("score").asDouble()) + "%";
                resorts.put(r.get("resort").asString(), score);
            }
            System.out.println(resorts);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return resorts;
    }

}
