package model;

import java.util.Date;

public class RushHourDataTO {

    private int id, roadId, distance, congestionLevel;
    private Date startTime, endTime;

    public RushHourDataTO(int id, int roadId, int distance, int congestionLevel, Date startTime, Date endTime) {
        this.id = id;
        this.roadId = roadId;
        this.distance = distance;
        this.congestionLevel = congestionLevel;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getCongestionLevel() {
        return congestionLevel;
    }

    public void setCongestionLevel(int congestionLevel) {
        this.congestionLevel = congestionLevel;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}
