package dao.impl;

import dao.ConnectionFactory;
import dao.RoadDao;
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
    public void insertRoad(RoadTO roadTO) {
        try {

            insertPreparedStmt.setInt(1, roadTO.getDistance());
            insertPreparedStmt.setInt(2, roadTO.getTravelTime());

            insertPreparedStmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Integer> getAllRoadIds() {
        try {
            ResultSet resultSet = getAllIdsStmt.executeQuery();

            List<Integer> roadIds = new ArrayList<>();
            while (resultSet.next()) {
                int id = resultSet.getInt("roadid");
                roadIds.add(id);
            }

            return roadIds;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public RoadTO getRoad(int roadId) {
        try {
            getRoadStmt.setInt(1, roadId);
            ResultSet resultSet = getRoadStmt.executeQuery();

            if (resultSet.first()) {
                int distance = resultSet.getInt("distance");
                int travelTime = resultSet.getInt("travelTime");

                return new RoadTO(roadId, distance, travelTime);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
