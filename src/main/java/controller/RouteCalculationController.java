package controller;

import com.graphhopper.routing.Dijkstra;
import com.graphhopper.routing.Path;
import com.graphhopper.routing.util.*;
import com.graphhopper.routing.weighting.ShortestWeighting;
import com.graphhopper.storage.GraphHopperStorage;
import com.graphhopper.util.EdgeIteratorState;
import dao.NodeDao;
import dao.impl.NodeDaoImpl;
import exception.DALException;
import integration.YrWeatherDataConnector;
import model.Node;
import model.Route;
import model.dto.NodeTO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RouteCalculationController {

    private NodeDao nodeDao;
    private YrWeatherDataConnector yrWeatherDataConnector;
    private DataController dataController;

    public RouteCalculationController() {
        try {
            this.nodeDao = new NodeDaoImpl();
            this.yrWeatherDataConnector = new YrWeatherDataConnector();
            this.dataController = new DataController();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // TODO: Adjust according to weather by checking weather forecast at dateTime
    public Route calculateRoute(double startLat, double startLon, double destinationLat, double destinationLon, Date dateTime) throws Exception {
        NodeTO startNode = nodeDao.getNodeByCoordinates(startLat, startLon);
        NodeTO destinationNode = nodeDao.getNodeByCoordinates(destinationLat, destinationLon);

        if (startNode == null || destinationNode == null)
            throw new DALException("Unknown nodes.");

        if (!nodeDao.isNodeOnRoad(startNode.getNodeId()))
            startNode = nodeDao.getClosestNodeOnRoad(startNode.getStreetName(), startLat, startLon);

        if (!nodeDao.isNodeOnRoad(destinationNode.getNodeId()))
            destinationNode = nodeDao.getClosestNodeOnRoad(destinationNode.getStreetName(), destinationLat, destinationLon);


        FlagEncoder encoder = new CarFlagEncoder();
        GraphHopperStorage graph = dataController.loadMapAsGraph(encoder);

        TraversalMode tMode = TraversalMode.NODE_BASED;
        Dijkstra dijkstra = new Dijkstra(graph, new ShortestWeighting(encoder), tMode);

        Path path = dijkstra.calcPath(startNode.getNodeId(), destinationNode.getNodeId());

        return mapPathToRoute(path, startNode, destinationNode);
    }

    private Route mapPathToRoute(Path path, NodeTO startNodeTO, NodeTO destinationNodeTO) throws DALException {
        List<Node> nodeList = new ArrayList<>();
        List<EdgeIteratorState> edges = path.calcEdges();
        int duration = 0;

        for (int i = 0; i < edges.size(); i++) {
            int baseId = edges.get(i).getBaseNode();
            int adjId = edges.get(i).getAdjNode();
            duration += edges.get(i).getDistance();

            NodeTO baseNodeTO = nodeDao.getNode(baseId);
            NodeTO adjNodeTO = nodeDao.getNode(adjId);

            Node baseNode = new Node(baseNodeTO.getNodeId(), baseNodeTO.getLatitude(), baseNodeTO.getLongitude());
            Node adjNode = new Node(adjNodeTO.getNodeId(), adjNodeTO.getLatitude(), adjNodeTO.getLongitude());

            if (!nodeList.contains(baseNode)) {
                nodeList.add(baseNode);
            }

            if (!nodeList.contains(adjNode)) {
                nodeList.add(adjNode);
            }
        }

        Node startNode = new Node(startNodeTO.getNodeId(), startNodeTO.getLatitude(), startNodeTO.getLongitude());
        Node destinationNode = new Node(destinationNodeTO.getNodeId(), destinationNodeTO.getLatitude(), destinationNodeTO.getLongitude());

        if (!nodeList.contains(startNode)) {
            nodeList.add(0, startNode);
        }

        if (!nodeList.contains(destinationNode)) {
            nodeList.add(destinationNode);
        }

        return new Route(nodeList, duration);
    }
}
