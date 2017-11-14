package dao.impl;

import dao.ConnectionFactory;
import dao.RoadDao;
import model.dto.RoadTO;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RoadDaoImpl implements RoadDao {

    private PreparedStatement insertPreparedStmt = null;

    public RoadDaoImpl() throws Exception {

        // Initialization of prepared statements
        insertPreparedStmt = ConnectionFactory.getConnection()
                .prepareStatement("INSERT INTO road (roadid, length, distance, travelTime) VALUES (?,?,?,?)");
    }

    @Override
    public void insertRoad(RoadTO roadTO) {
        try {

            insertPreparedStmt.setInt(1, roadTO.getRoadId());
            insertPreparedStmt.setInt(2, roadTO.getLength());
            insertPreparedStmt.setInt(3, roadTO.getDistance());
            insertPreparedStmt.setInt(4, roadTO.getTravelTime());

            insertPreparedStmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
