package dao;

import model.dto.RoadTO;

import java.util.List;

public interface RoadDao {

    void insertRoad(RoadTO roadTO);
    List<Integer> getAllRoadIds();
    RoadTO getRoad(int roadId);
}
