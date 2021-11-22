package com.thesis.server.repository;

import org.neo4j.driver.Driver;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.types.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.text.DecimalFormat;
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

    public Map<String, ResortDetails> getResorts() {
        Map<String, ResortDetails> resorts = new HashMap<>();

        try (Session session = driver.session()) {
            Result result = session.run("MATCH (m:Resort) RETURN m");
            List<Record> records = result.list();
            for (Record r : records) {
                Node node = r.get("m").asNode();
                resorts.put(node.get("name").asString(),
                        new ResortDetails(node.get("address").asString(), node.get("atol").asString()));
            }
            System.out.println(records);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return resorts;
    }

    public Map<String, String> surveyRecommendation(String query) {
        Map<String, String> resorts = new HashMap<>();
        try (Session session = driver.session()) {
            Result result = session.run(query);
            List<Record> records = result.list();
            System.out.println(records);
            for (Record r : records) {
                String score = df.format(r.get("score").asDouble());
                resorts.put(r.get("resort").asString(), score);
            }
            System.out.println(resorts);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return resorts;
    }

    public Map<String, ResortDetails> similarityRecommendation(String query) {
        Map<String, ResortDetails> resorts = new HashMap<>();

        try (Session session = driver.session()) {
            Result result = session.run(query);
            List<Record> records = result.list();
            for (Record r : records) {
                Node node = r.get("other").asNode();
                String score = df.format(r.get("jaccard_score").asDouble() * 100);
                resorts.put(node.get("name").asString(),
                        new ResortDetails(node.get("address").asString(), node.get("atol").asString(), score));
            }
            System.out.println(records);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return resorts;
    }

    //    public String buildQueryForResorts(List<String> options, String nodeType, String nodeAttribute) {
//        return "MATCH (m:Resort)--(n:" +
//                nodeType +
//                ") WHERE n." +
//                nodeAttribute +
//                " IN " +
//                buildOptions(options, "") +
//                " RETURN m";
//    }
//
//    public String buildQueryForResorts(String option, String nodeType, String relationshipName, String relationshipAttribute) {
//        return "MATCH (m:Resort)-[r:" +
//                relationshipName +
//                "]-(n:" +
//                nodeType +
//                ") WHERE r." +
//                relationshipAttribute +
//                " < " +
//                option +
//                " RETURN m";
//    }
}
