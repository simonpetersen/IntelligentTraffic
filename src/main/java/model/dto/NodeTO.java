package model.dto;

public class NodeTO {

    private int nodeId;
    private String type, streetName;
    private double latitude, longitude;

    public NodeTO(int nodeId, String type, double latitude, double longitude, String streetName) {
        this.nodeId = nodeId;
        this.type = type;
        this.latitude = latitude;
        this.longitude = longitude;
        this.streetName = streetName;
    }

    public int getNodeId() {
        return nodeId;
    }

    public void setNodeId(int nodeId) {
        this.nodeId = nodeId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }
}
