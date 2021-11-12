package com.thesis.server.repository;

import org.neo4j.driver.Driver;
import org.neo4j.driver.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class Neo4jRepository {

    private final Driver driver;

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
                buildOptions(options) +
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

    private String buildOptions(List<String> options) {
        String prefix = "";
        StringBuilder stringOptions = new StringBuilder("[");
        for (String c : options) {
            stringOptions.append(prefix);
            prefix = ",";
            stringOptions.append(c);
        }
        stringOptions.append("]");
        return stringOptions.toString();
    }
}
