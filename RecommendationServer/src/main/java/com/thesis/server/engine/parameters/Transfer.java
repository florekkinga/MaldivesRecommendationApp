package com.thesis.server.engine.parameters;

import java.util.List;

public class Transfer extends Parameter {
    private List<String> options;
    private Integer price;
    private Integer time;

    public List<String> getOptions() {
        return options;
    }

    public Integer getPrice() {
        return price;
    }

    public Integer getTime() {
        return time;
    }
}
