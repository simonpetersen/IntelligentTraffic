package model;

public class RoadNodesTO {

    private long id, roadId, nodeId;
    private int sequence;

    public RoadNodesTO(long id, long roadId, long nodeId, int sequence) {
        this.id = id;
        this.roadId = roadId;
        this.nodeId = nodeId;
        this.sequence = sequence;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getRoadId() {
        return roadId;
    }

    public void setRoadId(long roadId) {
        this.roadId = roadId;
    }

    public long getNodeId() {
        return nodeId;
    }

    public void setNodeId(long nodeId) {
        this.nodeId = nodeId;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }
}
