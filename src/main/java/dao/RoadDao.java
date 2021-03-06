package dao;

import exception.DALException;
import model.dto.RoadTO;

import java.util.List;

public interface RoadDao {

    void insertRoad(RoadTO roadTO) throws DALException;
    List<RoadTO> getAllRoads() throws DALException;
    RoadTO getRoad(int roadId) throws DALException;
}
