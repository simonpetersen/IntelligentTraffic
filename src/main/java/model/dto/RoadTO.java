package model.dto;

public class RoadTO {

    private int roadId, length, distance, travelTime;

    public RoadTO(int RoadId, int length, int distance, int travelTime) {
        this.roadId = RoadId;
        this.length = length;
        this.distance = distance;
        this.travelTime = travelTime;
    }

    public int getRoadId() {
        return roadId;
    }

    public void setRoadId(int roadId) {
        this.roadId = roadId;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getTravelTime() {
        return travelTime;
    }

    public void setTravelTime(int travelTime) {
        this.travelTime = travelTime;
    }
}
