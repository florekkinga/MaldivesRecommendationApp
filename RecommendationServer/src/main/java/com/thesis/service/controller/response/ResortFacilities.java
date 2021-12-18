package com.thesis.service.controller.response;

import java.util.List;

public class ResortFacilities extends Resort {
    public List<ResortAccommodation> accommodation;
    public List<ResortTransfer> transfer;
    public Integer starRating;
    public List<String> wineAndDine;
    public List<String> waterSports;
    public List<String> fitness;

    public ResortFacilities(
            String address, String atol, String score, String booking, Double userRating,
            List<ResortAccommodation> accommodation, List<ResortTransfer> transfer,
            Integer starRating, List<String> wineAndDine, List<String> waterSports, List<String> fitness)
    {
        super(address, atol, score, booking, userRating);
        this.accommodation = accommodation;
        this.transfer = transfer;
        this.starRating = starRating;
        this.wineAndDine = wineAndDine;
        this.waterSports = waterSports;
        this.fitness = fitness;
    }
}
