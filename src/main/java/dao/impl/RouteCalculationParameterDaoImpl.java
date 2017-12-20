package dao.impl;

import dao.ConnectionFactory;
import dao.RouteCalculationParameterDao;
import exception.DALException;
import model.dto.RouteCalculationParameterTO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class RouteCalculationParameterDaoImpl implements RouteCalculationParameterDao {

    private PreparedStatement getByCategoryAndBoundariesStmt;

    public RouteCalculationParameterDaoImpl() throws DALException {
        try {
            getByCategoryAndBoundariesStmt = ConnectionFactory.getConnection()
                    .prepareStatement("SELECT * FROM RouteCalculationParameter WHERE Category = ? AND LowerBound < ? AND ? <= UpperBound");
        } catch (Exception e) {
            throw new DALException(e.getMessage());
        }
    }

    @Override
    public RouteCalculationParameterTO getByCategoryAndBoundaries(String category, double value) throws DALException {
        try {
            getByCategoryAndBoundariesStmt.setString(1, category);
            getByCategoryAndBoundariesStmt.setDouble(2, value);
            getByCategoryAndBoundariesStmt.setDouble(3, value);

            ResultSet resultSet = getByCategoryAndBoundariesStmt.executeQuery();
            if (resultSet.first()) {
                String parameterName = resultSet.getString("ParameterName");
                double lowerBound = resultSet.getDouble("LowerBound");
                double upperBound = resultSet.getDouble("UpperBound");
                double impactFactor = resultSet.getDouble("ImpactFactor");

                return new RouteCalculationParameterTO(parameterName, category, lowerBound, upperBound, impactFactor);
            }

            return null;
        } catch (SQLException e) {
            throw new DALException(e.getMessage());
        }
    }
}
