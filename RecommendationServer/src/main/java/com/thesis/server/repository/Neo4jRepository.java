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

    public List<String> getResortsNames(List<String> category) {
        String prefix = "";
        StringBuilder category_options = new StringBuilder("[");
        for(String c: category){
            category_options.append(prefix);
            prefix = ",";
            category_options.append(c);
        }
        category_options.append("]");

        try (Session session = driver.session()) {
            return session.run("MATCH (m:Resort)--(r:StarRating) WHERE r.rating IN " + category_options + " RETURN m")
                    .list(r -> r.get("m").asNode().get("name").asString());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return new ArrayList<String>();
        }
    }
}
