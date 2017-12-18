package com.simcoder.uber.historyRecyclerView;

/**
 * Created by manel on 10/10/2017.
 */

public class HistoryModel {

    private String distance;
    private int rate;
    private double price;
    private String rideId;
    private String time;

    public HistoryModel() {
    }

    public HistoryModel(String rideId, String time) {
        this.rideId = rideId;
        this.time = time;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public String getRideId() {
        return rideId;
    }

    public void setRideId(String rideId) {
        this.rideId = rideId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getPrice() {
        return price;
    }
}
