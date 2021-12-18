package com.thesis.service.controller.response;

public class ResortAccommodation {
    public String type;
    public Integer halfBoard;
    public Integer fullBoard;
    public Integer allInclusive;

    public ResortAccommodation(String type, Integer halfBoard, Integer fullBoard, Integer allInclusive) {
        this.type = type;
        this.halfBoard = halfBoard;
        this.fullBoard = fullBoard;
        this.allInclusive = allInclusive;
    }
}
