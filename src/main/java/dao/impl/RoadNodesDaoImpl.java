package dao.impl;

import dao.ConnectionFactory;
import dao.RoadNodesDao;
import exception.DALException;
import model.dto.RoadNodesTO;

import javax.xml.transform.Result;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RoadNodesDaoImpl implements RoadNodesDao {

    private PreparedStatement insertListPreparedStmt, getRoadNodesIdsStmt, getRoadNodesStmt;

    public RoadNodesDaoImpl() throws Exception {

        // Initialization of prepared statements
        insertListPreparedStmt = ConnectionFactory.getConnection()
                .prepareStatement("INSERT INTO RoadNodes (road, node, sequence) VALUES (?,?,?)");

        getRoadNodesIdsStmt = ConnectionFactory.getConnection()
                .prepareStatement("SELECT RoadNodesId FROM RoadNodes WHERE Road = ?");

        getRoadNodesStmt = ConnectionFactory.getConnection()
                .prepareStatement("SELECT * FROM RoadNodes WHERE RoadNodesId = ?");
    }

    @Override
    public void insertRoadNodesList(List<RoadNodesTO> roadNodes) throws DALException {
        try {

            for (RoadNodesTO roadNode : roadNodes) {

                insertListPreparedStmt.setInt(1, roadNode.getRoadId());
                insertListPreparedStmt.setInt(2, roadNode.getNodeId());
                insertListPreparedStmt.setInt(3, roadNode.getSequence());

                insertListPreparedStmt.executeUpdate();
            }

        } catch (SQLException e) {
            throw new DALException(e.getMessage());
        }
    }

    @Override
    public List<Integer> getRoadNodesIds(int roadId) throws DALException {
        try {
            getRoadNodesIdsStmt.setInt(1, roadId);
            ResultSet resultSet = getRoadNodesIdsStmt.executeQuery();

            List<Integer> roadNodesIds = new ArrayList<>();
            while (resultSet.next()) {
                int id = resultSet.getInt("roadNodesId");
                roadNodesIds.add(id);
            }

            return roadNodesIds;
        } catch (SQLException e) {
            throw new DALException(e.getMessage());
        }
    }

    @Override
    public RoadNodesTO getRoadNodes(int roadNodesId) throws DALException {
        try {
            getRoadNodesStmt.setInt(1, roadNodesId);

            ResultSet resultSet = getRoadNodesStmt.executeQuery();
            if (resultSet.first()) {
                int roadId = resultSet.getInt("road");
                int nodeId = resultSet.getInt("node");
                int sequence = resultSet.getInt("sequence");

                return new RoadNodesTO(roadNodesId, roadId, nodeId, sequence);
            }

            throw new DALException("No roadnode found with RoadNodesId = " + roadNodesId);
        } catch (SQLException e) {
            throw new DALException(e.getMessage());
        }
    }
}
