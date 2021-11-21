package com.thesis.server.engine;

import com.thesis.server.engine.resort_parameters.*;

public class SurveyAnswers {
    private StarRating starRating;
    private Transfer transfer;
    private TransferPrice transferPrice;
    private TransferTime transferTime;
    private Accommodation accommodation;
    private AccommodationPrice accommodationPrice;
    private BoardBasis boardBasis;
    private WineAndDine wineAndDine;
    private WaterSports waterSports;
    private Fitness fitness;

    public StarRating getStarRating() {
        return starRating;
    }

    public Transfer getTransfer() {
        return transfer;
    }

    public TransferPrice getTransferPrice() {
        return transferPrice;
    }

    public TransferTime getTransferTime() {
        return transferTime;
    }

    public Accommodation getAccommodation() {
        return accommodation;
    }

    public AccommodationPrice getAccommodationPrice() {
        return accommodationPrice;
    }

    public BoardBasis getBoardBasis() {
        return boardBasis;
    }

    public WineAndDine getWineAndDine() {
        return wineAndDine;
    }

    public WaterSports getWaterSports() {
        return waterSports;
    }

    public Fitness getFitness() {
        return fitness;
    }
}
