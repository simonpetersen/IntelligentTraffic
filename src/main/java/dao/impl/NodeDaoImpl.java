package dao.impl;

import dao.ConnectionFactory;
import dao.NodeDao;
import exception.DALException;
import model.dto.NodeTO;
import util.TravelTimeCalculationUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NodeDaoImpl implements NodeDao {

    private PreparedStatement insertPreparedStmt, getNodeStmt, getAllIdsStmt, getNodeByCoordinatesStmt,
            getClosestNodeStmt, isNodeOnRoadStmt, countNodesStmt, getNodeSpeedLimitStmt;

    public NodeDaoImpl() throws Exception {

        // Initialization of prepared statements
        insertPreparedStmt = ConnectionFactory.getConnection()
                .prepareStatement("INSERT INTO Node (nodeid, type, latitude, longitude, streetName) VALUES (?,?,?,?,?)");

        getNodeStmt = ConnectionFactory.getConnection()
                .prepareStatement("SELECT * FROM Node WHERE NodeId = ?");

        getAllIdsStmt = ConnectionFactory.getConnection()
                .prepareStatement("SELECT NodeId FROM Node");

        getNodeByCoordinatesStmt = ConnectionFactory.getConnection()
                .prepareStatement("SELECT * FROM Node WHERE Latitude = ? AND Longitude = ?");

        getClosestNodeStmt = ConnectionFactory.getConnection()
                .prepareStatement("SELECT Node.NodeId, Node.Latitude, Node.Longitude FROM Node INNER JOIN Road, RoadNodes " +
                        "WHERE Road.StreetName = ? AND RoadNodes.Road = Road.RoadId AND RoadNodes.Node = Node.NodeId");

        isNodeOnRoadStmt = ConnectionFactory.getConnection()
                .prepareStatement("SELECT * FROM RoadNodes WHERE Node = ?");

        countNodesStmt = ConnectionFactory.getConnection()
                .prepareStatement("SELECT COUNT(*) FROM Node");

        getNodeSpeedLimitStmt = ConnectionFactory.getConnection()
                .prepareStatement("SELECT MaxSpeed From Road INNER JOIN RoadNodes WHERE RoadNodes.Road = Road.RoadId AND RoadNodes.Node = ?");
    }

    @Override
    public void insertNode(NodeTO nodeTO) throws DALException {
        try {

            insertPreparedStmt.setInt(1, nodeTO.getNodeId());
            insertPreparedStmt.setString(2, nodeTO.getType());
            insertPreparedStmt.setDouble(3, nodeTO.getLatitude());
            insertPreparedStmt.setDouble(4, nodeTO.getLongitude());
            insertPreparedStmt.setString(5, nodeTO.getStreetName());

            insertPreparedStmt.executeUpdate();

        } catch (SQLException e) {
            throw new DALException(e);
        }

    }

    @Override
    public NodeTO getNode(int nodeId) throws DALException {
        try {
            getNodeStmt.setInt(1, nodeId);

            ResultSet resultSet = getNodeStmt.executeQuery();
            if (resultSet.first()) {
                String type = resultSet.getString("type");
                double latitude = resultSet.getDouble("latitude");
                double longitude = resultSet.getDouble("longitude");
                String streetName = resultSet.getString("StreetName");

                return new NodeTO(nodeId, type, latitude, longitude, streetName);
            }

            throw new DALException("No node found with NodeId = "+nodeId);
        } catch (SQLException e) {
            throw new DALException(e.getMessage());
        }
    }

    @Override
    public List<Integer> getAllNodeIds() throws DALException {
        try {
            ResultSet resultSet = getAllIdsStmt.executeQuery();

            List<Integer> nodeIds = new ArrayList<>();

            while (resultSet.next()) {
                int id = resultSet.getInt("nodeid");
                nodeIds.add(id);
            }

            return nodeIds;
        } catch (SQLException e) {
            throw new DALException(e.getMessage());
        }
    }

    @Override
    public NodeTO getNodeByCoordinates(double latitude, double longitude) throws DALException {
        try {
            getNodeByCoordinatesStmt.setDouble(1, latitude);
            getNodeByCoordinatesStmt.setDouble(2, longitude);

            ResultSet resultSet = getNodeByCoordinatesStmt.executeQuery();

            if (resultSet.first()) {
                int id = resultSet.getInt("NodeId");
                String type = resultSet.getString("Type");
                double lat = resultSet.getDouble("Latitude");
                double lon = resultSet.getDouble("Longitude");
                String streetName = resultSet.getString("StreetName");

                return new NodeTO(id, type, lat, lon, streetName);
            }

            throw new DALException("No node found with coordinates = " + latitude + ", " + longitude);
        } catch (SQLException e) {
            throw new DALException(e.getMessage());
        }
    }

    @Override
    public NodeTO getClosestNodeOnRoad(String streetName, double latitude, double longitude) throws DALException {
        try {
            getClosestNodeStmt.setString(1, streetName);

            ResultSet resultSet = getClosestNodeStmt.executeQuery();
            if (resultSet.first()) {
                NodeTO node = new NodeTO(0, null, latitude, longitude, streetName);
                double nodeLatitude = resultSet.getDouble("Latitude");
                double nodeLongitude = resultSet.getDouble("Longitude");
                int closestNodeId = resultSet.getInt("NodeId");
                NodeTO closestNode = new NodeTO(closestNodeId, null, nodeLatitude, nodeLongitude, streetName);
                double smallestDist = TravelTimeCalculationUtil.calcDist(node, closestNode);

                while (resultSet.next()) {
                    double nextLatitude = resultSet.getDouble("Latitude");
                    double nextLongitude = resultSet.getDouble("Longitude");
                    int nextNodeId = resultSet.getInt("NodeId");
                    NodeTO nextNode = new NodeTO(nextNodeId, null, nextLatitude, nextLongitude, streetName);
                    double distance = TravelTimeCalculationUtil.calcDist(node, nextNode);

                    if (distance < smallestDist) {
                        closestNode = nextNode;
                        smallestDist = distance;
                    }
                }

                return closestNode;
            }

            throw new DALException(streetName + " is not a valid street name.");

        } catch (SQLException e) {
            throw new DALException(e.getMessage());
        }
    }

    @Override
    public boolean isNodeOnRoad(int nodeId) throws DALException {
        try {
            isNodeOnRoadStmt.setInt(1, nodeId);

            ResultSet resultSet = isNodeOnRoadStmt.executeQuery();

            if (resultSet.first())
                return true;

            return false;
        } catch (SQLException e) {
            throw new DALException(e.getMessage());
        }
    }

    @Override
    public int getNumberOfNodes() throws DALException {
        try {
            ResultSet resultSet = countNodesStmt.executeQuery();

            if (resultSet.first()) {
                return resultSet.getInt("Count(*)");
            }

            throw new DALException("Error in query.");
        } catch (SQLException e) {
            throw new DALException(e.getMessage());
        }
    }

    @Override
    public int getNodeSpeedLimit(int nodeId) throws DALException {
        try {
            getNodeSpeedLimitStmt.setInt(1, nodeId);
            ResultSet resultSet = getNodeSpeedLimitStmt.executeQuery();

            if(resultSet.first()){
                return resultSet.getInt("MaxSpeed");
            }
            return 0;
        }
        catch (SQLException e){
            throw new DALException(e.getMessage());
        }
    }
}
