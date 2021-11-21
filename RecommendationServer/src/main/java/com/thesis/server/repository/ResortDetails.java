package com.thesis.server.repository;

public class ResortDetails {
    public String address;
    public String atol;
    public String score;
    String url;

    public ResortDetails(String address, String atol, String score){
        this.address = address;
        this.atol = atol;
        this.score = score;
    }

    public ResortDetails(String address, String atol){
        this.address = address;
        this.atol = atol;
    }
}
