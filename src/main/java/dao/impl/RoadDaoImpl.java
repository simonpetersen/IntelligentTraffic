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

    private PreparedStatement insertPreparedStmt, getAllIdsStmt, getRoadStmt;

    public RoadDaoImpl() throws Exception {

        // Initialization of prepared statements
        insertPreparedStmt = ConnectionFactory.getConnection()
                .prepareStatement("INSERT INTO Road (distance, travelTime) VALUES (?,?)");

        getAllIdsStmt = ConnectionFactory.getConnection()
                .prepareStatement("SELECT roadId FROM Road");

        getRoadStmt = ConnectionFactory.getConnection()
                .prepareStatement("SELECT * FROM Road WHERE RoadId = ?");
    }

    @Override
    public int insertRoad(RoadTO roadTO) throws DALException {
        try {
            insertPreparedStmt.setInt(1, roadTO.getDistance());
            insertPreparedStmt.setInt(2, roadTO.getTravelTime());

            return insertPreparedStmt.executeUpdate();
        } catch (SQLException e) {
            throw new DALException(e.getMessage());
        }
    }

    @Override
    public List<Integer> getAllRoadIds() throws DALException {
        try {
            ResultSet resultSet = getAllIdsStmt.executeQuery();

            List<Integer> roadIds = new ArrayList<>();
            while (resultSet.next()) {
                int id = resultSet.getInt("roadid");
                roadIds.add(id);
            }

            return roadIds;
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
                int travelTime = resultSet.getInt("travelTime");

                return new RoadTO(roadId, distance, travelTime);
            }

            throw new DALException("No road found with roadId = " + roadId);
        } catch (SQLException e) {
            throw new DALException(e.getMessage());
        }
    }
}
