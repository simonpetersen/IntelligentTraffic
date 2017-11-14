package dao.impl;

import dao.ConnectionFactory;
import dao.NodeDao;
import model.dto.NodeTO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class NodeDaoImpl implements NodeDao {

    private PreparedStatement insertPreparedStmt, getNodeStmt, getAllIdsStmt;

    public NodeDaoImpl() throws Exception {

        // Initialization of prepared statements
        insertPreparedStmt = ConnectionFactory.getConnection()
                .prepareStatement("INSERT INTO node (nodeid, type, latitude, longitude) VALUES (?,?,?,?)");

        getNodeStmt = ConnectionFactory.getConnection()
                .prepareStatement("SELECT * FROM node WHERE NodeId = ?");

        getAllIdsStmt = ConnectionFactory.getConnection()
                .prepareStatement("SELECT NodeId FROM node");
    }

    @Override
    public void insertNode(NodeTO nodeTO) {
        try {

            insertPreparedStmt.setInt(1, nodeTO.getNodeId());
            insertPreparedStmt.setString(2, nodeTO.getType());
            insertPreparedStmt.setDouble(3, nodeTO.getLatitude());
            insertPreparedStmt.setDouble(4, nodeTO.getLongitude());

            insertPreparedStmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public NodeTO getNode(int nodeId) {
        try {
            getNodeStmt.setInt(1, nodeId);

            ResultSet resultSet = getNodeStmt.executeQuery();
            if (resultSet.first()) {
                String type = resultSet.getString("type");
                double latitude = resultSet.getDouble("latitude");
                double longitude = resultSet.getDouble("longitude");

                return new NodeTO(nodeId, type, latitude, longitude);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<Integer> getAllNodeIds() {
        try {
            ResultSet resultSet = getAllIdsStmt.executeQuery();

            List<Integer> nodeIds = new ArrayList<>();
            while (resultSet.next()) {
                int id = resultSet.getInt("nodeid");
                nodeIds.add(id);
            }

            return nodeIds;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
