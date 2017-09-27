package dao;

import model.RoadNodesTO;

import java.util.List;

public interface RoadNodesDao {

    void insertRoadNodesList(List<RoadNodesTO> roadNodes);
}
