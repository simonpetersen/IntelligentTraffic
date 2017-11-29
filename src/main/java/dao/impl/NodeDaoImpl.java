package dao.impl;

import dao.ConnectionFactory;
import dao.NodeDao;
import exception.DALException;
import model.dto.NodeTO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NodeDaoImpl implements NodeDao {

    private PreparedStatement insertPreparedStmt, getNodeStmt, getAllIdsStmt, getNodeByCoordinatesStmt;

    public NodeDaoImpl() throws Exception {

        // Initialization of prepared statements
        insertPreparedStmt = ConnectionFactory.getConnection()
                .prepareStatement("INSERT INTO Node (nodeid, type, latitude, longitude) VALUES (?,?,?,?)");

        getNodeStmt = ConnectionFactory.getConnection()
                .prepareStatement("SELECT * FROM Node WHERE NodeId = ?");

        getAllIdsStmt = ConnectionFactory.getConnection()
                .prepareStatement("SELECT NodeId FROM Node");

        getNodeByCoordinatesStmt = ConnectionFactory.getConnection()
                .prepareStatement("SELECT * FROM Node WHERE Latitude = ? AND Longitude = ?");
    }

    @Override
    public void insertNode(NodeTO nodeTO) throws DALException {
        try {

            insertPreparedStmt.setInt(1, nodeTO.getNodeId());
            insertPreparedStmt.setString(2, nodeTO.getType());
            insertPreparedStmt.setDouble(3, nodeTO.getLatitude());
            insertPreparedStmt.setDouble(4, nodeTO.getLongitude());

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

                return new NodeTO(nodeId, type, latitude, longitude);
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

                return new NodeTO(id, type, lat, lon);
            }

            throw new DALException("No node found with coordinates = " + latitude + ", " + longitude);
        } catch (SQLException e) {
            throw new DALException(e.getMessage());
        }
    }
}
