package dao.impl;

import dao.ConnectionFactory;
import dao.RoadDao;
import exception.DALException;
import model.dto.NodeTO;
import model.dto.RoadTO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class RoadDaoImpl implements RoadDao {

    private PreparedStatement insertPreparedStmt, getAllStmt, getRoadStmt;

    public RoadDaoImpl() throws Exception {

        // Initialization of prepared statements
        insertPreparedStmt = ConnectionFactory.getConnection()
                .prepareStatement("INSERT INTO Road (roadId, distance, streetName, oneWay, maxSpeed) VALUES (?,?,?,?,?)");

        getAllStmt = ConnectionFactory.getConnection()
                .prepareStatement("SELECT * FROM Road");

        getRoadStmt = ConnectionFactory.getConnection()
                .prepareStatement("SELECT * FROM Road WHERE RoadId = ?");
    }

    @Override
    public void insertRoad(RoadTO roadTO) throws DALException {
        try {
            insertPreparedStmt.setInt(1, roadTO.getRoadId());
            insertPreparedStmt.setDouble(2, roadTO.getDistance());
            insertPreparedStmt.setString(3, roadTO.getStreetName());
            insertPreparedStmt.setBoolean(4, roadTO.isOneWay());
            insertPreparedStmt.setInt(5, roadTO.getMaxSpeed());

            insertPreparedStmt.executeUpdate();
        } catch (SQLException e) {
            throw new DALException(e.getMessage());
        }
    }

    @Override
    public List<RoadTO> getAllRoads() throws DALException {
        try {
            ResultSet resultSet = getAllStmt.executeQuery();

            List<RoadTO> roads = new ArrayList<>();
            while (resultSet.next()) {
                int id = resultSet.getInt("roadid");
                int distance = resultSet.getInt("distance");
                String streetName = resultSet.getString("streetName");
                boolean oneWay = resultSet.getBoolean("oneWay");
                int maxSpeed = resultSet.getInt("maxSpeed");

                roads.add(new RoadTO(id, distance, streetName, oneWay, maxSpeed));
            }

            return roads;
        } catch (SQLException e) {
            throw new DALException(e.getMessage());
        }
    }

    @Override
    public RoadTO getRoad(int roadId) throws DALException {
        try {
            getRoadStmt.setInt(1, roadId);
            ResultSet resultSet = getRoadStmt.executeQuery();

            if (resultSet.first()) {
                int distance = resultSet.getInt("distance");
                String streetName = resultSet.getString("streetName");
                boolean oneWay = resultSet.getBoolean("oneWay");
                int maxSpeed = resultSet.getInt("maxSpeed");

                return new RoadTO(roadId, distance, streetName, oneWay, maxSpeed);
            }

            throw new DALException("No road found with roadId = " + roadId);
        } catch (SQLException e) {
            throw new DALException(e.getMessage());
        }
    }
}
