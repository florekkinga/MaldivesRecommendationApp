package com.thesis.service.controller.response;

public class ResortTransfer {
    public String name;
    public Integer price;
    public Integer time;

    public ResortTransfer(String name, Integer price, Integer time) {
        this.name = name;
        this.price = price;
        this.time = time;
    }
}
