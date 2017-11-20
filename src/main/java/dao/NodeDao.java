package dao;

import model.dto.NodeTO;

import java.util.List;

public interface NodeDao {

    void insertNode(NodeTO nodeTO);
    NodeTO getNode(int nodeId);
    List<Integer> getAllNodeIds();
    NodeTO getNodeByCoordinates(double latitude, double longitude);
}
