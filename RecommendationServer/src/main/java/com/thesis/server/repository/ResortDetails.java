package com.thesis.server.repository;

public class ResortDetails {
    public String address;
    public String atol;
    public String score;
    public String booking;
    public Double userRating;

    public ResortDetails(String address, String atol, String score, String booking, Double userRating){
        this.address = address;
        this.atol = atol;
        this.score = score;
        this.booking = booking;
        this.userRating = userRating;
    }
}
