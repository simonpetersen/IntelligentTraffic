package dao;

import model.dto.RoadNodesTO;

import java.util.List;

public interface RoadNodesDao {

    void insertRoadNodesList(List<RoadNodesTO> roadNodes);
    List<Integer> getRoadNodesIds(int roadId);
    RoadNodesTO getRoadNodes(int roadNodesId);
}
