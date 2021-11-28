package com.thesis.server.repository;

public class AccommodationRepository {
    public String type;
    public Integer halfBoard;
    public Integer fullBoard;
    public Integer allInclusive;

    public AccommodationRepository(String type, Integer halfBoard, Integer fullBoard, Integer allInclusive) {
        this.type = type;
        this.halfBoard = halfBoard;
        this.fullBoard = fullBoard;
        this.allInclusive = allInclusive;
    }
}
