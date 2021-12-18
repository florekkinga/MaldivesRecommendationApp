package com.thesis.service.repository;

import com.thesis.service.controller.response.ResortAccommodation;
import com.thesis.service.controller.response.Resort;
import com.thesis.service.controller.response.ResortFacilities;
import com.thesis.service.controller.response.ResortTransfer;
import org.neo4j.driver.Driver;
import org.neo4j.driver.Record;
import org.neo4j.driver.Result;
import org.neo4j.driver.Session;
import org.neo4j.driver.types.Node;
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

    public Map<String, Resort> getResorts() {
        Map<String, Resort> resorts = new HashMap<>();

        try (Session session = driver.session()) {
            Result result = session.run("MATCH (m:Resort) RETURN m");
            List<Record> records = result.list();
            for (Record r : records) {
                Node node = r.get("m").asNode();
                Double rating = node.get("tripAdvisorRating").asDouble();
                resorts.put(node.get("name").asString(),
                        new Resort(node.get("address").asString(), node.get("atol").asString(), "",
                                node.get("booking").asString(), rating));
            }
            System.out.println(records);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return resorts;
    }

    public Map<String, Resort> surveyRecommendation(String query) {
        Map<String, Resort> resorts = new HashMap<>();
        try (Session session = driver.session()) {
            Result result = session.run(query);
            List<Record> records = result.list();
            System.out.println(records);
            for (Record r : records) {
                Node node = r.get("r").asNode();
                String score = df.format(r.get("score").asDouble());
                Double rating = node.get("tripAdvisorRating").asDouble();
                resorts.put(node.get("name").asString(),
                        new Resort(node.get("address").asString(), node.get("atol").asString(), score,
                                node.get("booking").asString(), rating));
            }
            System.out.println(resorts);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return resorts;
    }

    public Map<String, Resort> similarityRecommendation(String query) {
        Map<String, Resort> resorts = new HashMap<>();

        try (Session session = driver.session()) {
            Result result = session.run(query);
            List<Record> records = result.list();
            for (Record r : records) {
                Node node = r.get("other").asNode();
                String score = df.format(r.get("jaccard_score").asDouble() * 100);
                Double rating = node.get("tripAdvisorRating").asDouble();
                resorts.put(node.get("name").asString(),
                        new Resort(node.get("address").asString(), node.get("atol").asString(), score,
                                node.get("booking").asString(), rating));
            }
            System.out.println(records);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return resorts;
    }

    public ResortFacilities getResortDetailsAndFacilities(String name) {

        String query = "MATCH (r:Resort)--(f) \n" +
                "WHERE r.name = \"" + name + "\" \n" +
                "return r, f";

        try (Session session = driver.session()) {
            Node resort_node = null;
            List<ResortAccommodation> accommodation = new ArrayList<>();
            List<ResortTransfer> transfer = new ArrayList<>();
            Integer starRating = null;
            List<String> wineAndDine = new ArrayList<>();
            List<String> waterSports = new ArrayList<>();
            List<String> fitness = new ArrayList<>();

            Result result = session.run(query);
            List<Record> records = result.list();
            for (Record r : records) {
                resort_node = r.get("r").asNode();
                Node facility_node = r.get("f").asNode();
                if(facility_node.hasLabel("StarRating")){
                    starRating = facility_node.get("rating").asInt();
                }
                else if(facility_node.hasLabel("Accommodation")){
//                    Integer halfBoard = !facility_node.get("halfBoard").isNull() ? facility_node.get("halfBoard").asInt() : null;
//                    Integer fullBoard = !facility_node.get("fullBoard").isNull() ? facility_node.get("fullBoard").asInt() : null;
//                    Integer allInclusive = !facility_node.get("allInclusive").isNull() ? facility_node.get("allInclusive").asInt() : null;
                    accommodation.add(new ResortAccommodation(facility_node.get("type").asString(),
                            null, null, null));
                }
                else if(facility_node.hasLabel("Transfer")){
                    transfer.add(new ResortTransfer(facility_node.get("type").asString(), null, null));
                }
                else if(facility_node.hasLabel("WaterSports")){
                    waterSports.add(facility_node.get("name").asString());
                }
                else if(facility_node.hasLabel("WineAndDine")){
                    wineAndDine.add(facility_node.get("type").asString());
                }
                else if(facility_node.hasLabel("Fitness")){
                    fitness.add(facility_node.get("name").asString());
                }
            }

            System.out.println(records);
            if(resort_node != null){
                String address = resort_node.get("address").asString();
                String atol = resort_node.get("atol").asString();
                String booking = resort_node.get("booking").asString();
                double tripAdvisorRating = resort_node.get("tripAdvisorRating").asDouble();

                return new ResortFacilities(address, atol, "", booking, tripAdvisorRating, accommodation, transfer, starRating, wineAndDine, waterSports, fitness);
            }
            return null;

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
