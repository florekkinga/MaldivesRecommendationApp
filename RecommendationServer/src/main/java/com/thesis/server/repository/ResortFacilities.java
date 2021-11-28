package com.thesis.server.repository;

import java.util.List;

public class ResortFacilities extends Resort {
    public List<AccommodationRepository> accommodation;
    public List<TransferRepository> transfer;
    public Integer starRating;
    public List<String> wineAndDine;
    public List<String> waterSports;
    public List<String> fitness;

    public ResortFacilities(
            String address, String atol, String score, String booking, Double userRating,
            List<AccommodationRepository> accommodation, List<TransferRepository> transfer,
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
