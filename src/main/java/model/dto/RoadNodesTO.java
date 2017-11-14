package model.dto;

public class RoadNodesTO {

    private int roadNodesId, sequence, nodeId, roadId;

    public RoadNodesTO(int roadNodesId, int roadId, int nodeId, int sequence) {
        this.roadNodesId = roadNodesId;
        this.roadId = roadId;
        this.nodeId = nodeId;
        this.sequence = sequence;
    }

    public int getRoadNodesId() {
        return roadNodesId;
    }

    public void setRoadNodesId(int roadNodesId) {
        this.roadNodesId = roadNodesId;
    }

    public int getRoadId() {
        return roadId;
    }

    public void setRoadId(int roadId) {
        this.roadId = roadId;
    }

    public int getNodeId() {
        return nodeId;
    }

    public void setNodeId(int nodeId) {
        this.nodeId = nodeId;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }
}
