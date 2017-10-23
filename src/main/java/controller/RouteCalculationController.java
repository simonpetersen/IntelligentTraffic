package controller;

import dao.NodeDao;
import dao.RoadDao;
import dao.RoadNodesDao;
import dao.impl.NodeDaoImpl;
import dao.impl.RoadDaoImpl;
import dao.impl.RoadNodesDaoImpl;
import integration.YrWeatherDataConnector;
import model.Node;
import model.Route;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RouteCalculationController {

    private NodeDao nodeDao;
    private RoadNodesDao roadNodesDao;
    private RoadDao roadDao;
    private YrWeatherDataConnector yrWeatherDataConnector;

    public RouteCalculationController() {
        try {
            this.nodeDao = new NodeDaoImpl();
            this.roadNodesDao = new RoadNodesDaoImpl();
            this.roadDao = new RoadDaoImpl();
            this.yrWeatherDataConnector = new YrWeatherDataConnector();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // TODO: Implement
    public Route calculateRoute(Node start, Node destination, Date dateTime) {
        // For testing purposes a simple route is returned
        return createMockRoute();
    }

    private Route createMockRoute() {
        List<Node> nodes = new ArrayList<>();
        nodes.add(new Node(147749, 54.7808636, 11.4887596));
        nodes.add(new Node(147750, 54.7788018, 11.4809834));
        nodes.add(new Node(147751, 54.7770637, 11.4743413));
        return new Route(nodes);
    }
}
