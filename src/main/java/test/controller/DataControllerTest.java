package test.controller;

import com.graphhopper.routing.util.CarFlagEncoder;
import com.graphhopper.routing.util.FlagEncoder;
import com.graphhopper.storage.GraphHopperStorage;
import controller.DataController;
import dao.NodeDao;
import dao.impl.NodeDaoImpl;
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
        double latitudePoint1 = 54.7746152;
        double longitudePoint1 = 11.5020717;
        double latitudePoint2 = 54.7747045;
        double longitudePoint2 = 11.5030908;

        // Calculated the distance between the first two nodes of Østergade.
        double calculatedDistance = TravelTimeCalculationUtil.calcDist(latitudePoint1, longitudePoint1, latitudePoint2, longitudePoint2);
        double expectedDistance = 66.11; // meters

        assertEquals(expectedDistance, calculatedDistance, 0.01);
    }
}
