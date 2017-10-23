package dao.impl;

import dao.ConnectionFactory;
import dao.RoadNodesDao;
import model.dto.RoadNodesTO;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class RoadNodesDaoImpl implements RoadNodesDao {

    private PreparedStatement insertListPreparedStmt = null;

    public RoadNodesDaoImpl() throws Exception {

        // Initialization of prepared statements
        insertListPreparedStmt = ConnectionFactory.getConnection()
                .prepareStatement("INSERT INTO roadnodes (road, node, sequence) VALUES (?,?,?)");
    }

    @Override
    public void insertRoadNodesList(List<RoadNodesTO> roadNodes) {
        try {

            for (RoadNodesTO roadNode : roadNodes) {

                insertListPreparedStmt.setLong(1, roadNode.getRoadId());
                insertListPreparedStmt.setLong(2, roadNode.getNodeId());
                insertListPreparedStmt.setInt(3, roadNode.getSequence());

                insertListPreparedStmt.executeUpdate();

            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
