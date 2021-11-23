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
                Double rating = node.get("tripAdvisorRating").asDouble();
                resorts.put(node.get("name").asString(),
                        new ResortDetails(node.get("address").asString(), node.get("atol").asString(), "",
                                node.get("booking").asString(), rating));
            }
            System.out.println(records);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return resorts;
    }

    public Map<String, ResortDetails> surveyRecommendation(String query) {
        Map<String, ResortDetails> resorts = new HashMap<>();
        try (Session session = driver.session()) {
            Result result = session.run(query);
            List<Record> records = result.list();
            System.out.println(records);
            for (Record r : records) {
                Node node = r.get("r").asNode();
                String score = df.format(r.get("score").asDouble());
                Double rating = node.get("tripAdvisorRating").asDouble();
                resorts.put(node.get("name").asString(),
                        new ResortDetails(node.get("address").asString(), node.get("atol").asString(), score,
                                node.get("booking").asString(), rating));
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
                Double rating = node.get("tripAdvisorRating").asDouble();
                resorts.put(node.get("name").asString(),
                        new ResortDetails(node.get("address").asString(), node.get("atol").asString(), score,
                                node.get("booking").asString(), rating));
            }
            System.out.println(records);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return resorts;
    }
}
