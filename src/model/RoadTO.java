package model;

public class RoadTO {

    private int id, startNode, stopNode, length, speedLimit, zipCode;

    public RoadTO(int id, int startNode, int stopNode, int length, int speedLimit, int zipCode) {
        this.id = id;
        this.startNode = startNode;
        this.stopNode = stopNode;
        this.length = length;
        this.speedLimit = speedLimit;
        this.zipCode = zipCode;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStartNode() {
        return startNode;
    }

    public void setStartNode(int startNode) {
        this.startNode = startNode;
    }

    public int getStopNode() {
        return stopNode;
    }

    public void setStopNode(int stopNode) {
        this.stopNode = stopNode;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getSpeedLimit() {
        return speedLimit;
    }

    public void setSpeedLimit(int speedLimit) {
        this.speedLimit = speedLimit;
    }

    public int getZipCode() {
        return zipCode;
    }

    public void setZipCode(int zipCode) {
        this.zipCode = zipCode;
    }
}
