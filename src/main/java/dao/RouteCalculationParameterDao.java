package dao;

import exception.DALException;
import model.dto.RouteCalculationParameterTO;

public interface RouteCalculationParameterDao {

    RouteCalculationParameterTO getByCategoryAndBoundaries(String category, double value) throws DALException;
}
