package dao;

import exception.DALException;
import model.dto.NodeTO;

import java.util.List;

public interface NodeDao {

    void insertNode(NodeTO nodeTO) throws DALException;
    NodeTO getNode(int nodeId) throws DALException;
    List<Integer> getAllNodeIds() throws DALException;
    NodeTO getNodeByCoordinates(double latitude, double longitude) throws DALException;
    NodeTO getClosestNodeOnRoad(String streetName, double latitude, double longitude) throws DALException;
    boolean isNodeOnRoad(int nodeId) throws DALException;
    int getNumberOfNodes() throws DALException;
}
