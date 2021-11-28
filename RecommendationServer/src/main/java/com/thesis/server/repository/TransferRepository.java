package com.thesis.server.repository;

public class TransferRepository {
    public String name;
    public Integer price;
    public Integer time;

    public TransferRepository(String name, Integer price, Integer time) {
        this.name = name;
        this.price = price;
        this.time = time;
    }
}
