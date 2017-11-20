package model.dto;

public class RoadTO {

    private int roadId, distance, travelTime;

    public RoadTO(int RoadId, int distance, int travelTime) {
        this.roadId = RoadId;
        this.distance = distance;
        this.travelTime = travelTime;
    }

    public int getRoadId() {
        return roadId;
    }

    public void setRoadId(int roadId) {
        this.roadId = roadId;
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
