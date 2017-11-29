package dao;

import exception.DALException;
import model.dto.RoadNodesTO;

import java.util.List;

public interface RoadNodesDao {

    void insertRoadNodesList(List<RoadNodesTO> roadNodes) throws DALException;
    List<Integer> getRoadNodesIds(int roadId) throws DALException;
    RoadNodesTO getRoadNodes(int roadNodesId) throws DALException;
}
