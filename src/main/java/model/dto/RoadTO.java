package model.dto;

public class RoadTO {

    private int roadId, maxSpeed;
    private double distance;
    private String streetName;
    private boolean oneWay;

    public RoadTO(int RoadId, double distance, String streetName, boolean oneWay, int maxSpeed) {
        this.roadId = RoadId;
        this.distance = distance;
        this.streetName = streetName;
        this.oneWay = oneWay;
        this.maxSpeed = maxSpeed;
    }

    public int getRoadId() {
        return roadId;
    }

    public void setRoadId(int roadId) {
        this.roadId = roadId;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public boolean isOneWay() {
        return oneWay;
    }

    public void setOneWay(boolean oneWay) {
        this.oneWay = oneWay;
    }

    public int getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(int maxSpeed) {
        this.maxSpeed = maxSpeed;
    }
}
