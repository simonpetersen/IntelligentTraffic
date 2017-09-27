package dao.impl;

import dao.ConnectionFactory;
import dao.RoadDao;
import model.RoadTO;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class RoadDaoImpl implements RoadDao {

    private PreparedStatement insertPreparedStmt = null;

    public RoadDaoImpl() throws Exception {

        // Initialization of prepared statements
        insertPreparedStmt = ConnectionFactory.getConnection()
                .prepareStatement("INSERT INTO road (id, length, speedLimit, zipCode) VALUES (?,?,?,?)");
    }

    @Override
    public void insertRoad(RoadTO roadTO) {
        try {

            insertPreparedStmt.setLong(1, roadTO.getId());
            insertPreparedStmt.setInt(2, roadTO.getLength());
            insertPreparedStmt.setInt(3, roadTO.getSpeedLimit());
            insertPreparedStmt.setInt(4, roadTO.getZipCode());

            insertPreparedStmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
