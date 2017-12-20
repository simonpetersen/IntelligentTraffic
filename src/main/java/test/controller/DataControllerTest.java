package test.controller;

import com.graphhopper.routing.util.CarFlagEncoder;
import com.graphhopper.routing.util.FlagEncoder;
import com.graphhopper.storage.GraphHopperStorage;
import controller.DataController;
import dao.NodeDao;
import dao.impl.NodeDaoImpl;
import model.dto.NodeTO;
import org.junit.Before;
import org.junit.Test;
import util.TravelTimeCalculationUtil;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class DataControllerTest {

    private DataController dataController;
    private NodeDao nodeDao;

    @Before
    public void setUp() throws Exception {
        dataController = new DataController();
        nodeDao = new NodeDaoImpl();
    }

    @Test
    public void testLoadGraph() throws Exception {
        double travelTimeReductionFactor = 1;
        int numberOfNodes = nodeDao.getNumberOfNodes();

        if (numberOfNodes == 0) {
            dataController.saveMapInDB();
        }

        FlagEncoder encoder = new CarFlagEncoder();
        GraphHopperStorage graph = dataController.loadMapAsGraph(encoder, travelTimeReductionFactor);

        int expectedNodeCount = 12832;
        int actualNodeCount = graph.getNodes() - 1;
        assertEquals(expectedNodeCount, actualNodeCount);

        int expectedEdgeCount = 6782;
        int actualEdgeCount = graph.getAllEdges().getMaxId();
        assertEquals(expectedEdgeCount, actualEdgeCount);

    }

    @Test
    public void testCalculateDistance() {
        NodeTO startNode = new NodeTO(1, null, 54.7746152, 11.5020717, null);
        NodeTO destinationNode = new NodeTO(2, null, 54.7747045, 11.5030908, null);

        // Calculated the distance between the first two nodes of Østergade.
        double calculatedDistance = TravelTimeCalculationUtil.calcDist(startNode, destinationNode);
        double expectedDistance = 66.11; // meters

        assertEquals(expectedDistance, calculatedDistance, 0.01);
    }
}
