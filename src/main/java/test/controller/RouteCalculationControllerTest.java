package test.controller;

import com.graphhopper.routing.Path;
import com.graphhopper.routing.util.CarFlagEncoder;
import com.graphhopper.routing.util.FlagEncoder;
import com.graphhopper.storage.GraphHopperStorage;
import com.graphhopper.util.EdgeIteratorState;
import com.graphhopper.util.InstructionList;
import com.graphhopper.util.Translation;
import com.graphhopper.util.TranslationMap;
import controller.RouteCalculationController;
import dao.NodeDao;
import dao.impl.NodeDaoImpl;
import model.Route;
import model.dto.NodeTO;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class RouteCalculationControllerTest {

    private RouteCalculationController routeCalculationController;
    private NodeDao nodeDao;

    @Before
    public void setUp() throws Exception {
        routeCalculationController = new RouteCalculationController();
        nodeDao = new NodeDaoImpl();
    }

    @Test
    public void testCalculateRoute() throws Exception {
        NodeTO startNode = nodeDao.getNode(1);
        NodeTO destinationNode = nodeDao.getNode(2);
        Route route = routeCalculationController.calculateRoute(startNode.getLatitude(), startNode.getLongitude(),
                destinationNode.getLatitude(), destinationNode.getLongitude(), new Date());

        assertNotNull(route);
        assertTrue(!route.getNodes().isEmpty());

        /*
        for (EdgeIteratorState edge : path.calcEdges()) {
            System.out.println(edge.getBaseNode() + " to " + edge.getAdjNode());
        }

        Translation translation = new TranslationMap().get("da_DK");
        InstructionList instructionList = path.calcInstructions(translation);
        for (int i = 0; i < instructionList.size(); i++) {
            System.out.println(instructionList.get(i).getName());
            System.out.println("--------------------");
        }
        */
    }
}
