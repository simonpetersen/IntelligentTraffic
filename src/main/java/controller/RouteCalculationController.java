package controller;

import com.graphhopper.routing.Dijkstra;
import com.graphhopper.routing.Path;
import com.graphhopper.routing.util.*;
import com.graphhopper.routing.weighting.ShortestWeighting;
import com.graphhopper.storage.GraphHopperStorage;
import com.graphhopper.util.EdgeIteratorState;
import dao.NodeDao;
import dao.RouteCalculationParameterDao;
import dao.impl.NodeDaoImpl;
import dao.impl.RouteCalculationParameterDaoImpl;
import exception.DALException;
import integration.YrWeatherDataConnector;
import integration.xml.TimeElement;
import integration.xml.YrWeatherDataXml;
import model.Node;
import model.Route;
import model.dto.NodeTO;
import model.dto.RouteCalculationParameterTO;
import model.Weather;
import util.ParameterCategoryValues;
import util.TravelTimeCalculationUtil;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class RouteCalculationController {

    private NodeDao nodeDao;
    private RouteCalculationParameterDao routeCalculationParameterDao;
    private YrWeatherDataConnector yrWeatherDataConnector;
    private DataController dataController;

    public RouteCalculationController() {
        try {
            this.nodeDao = new NodeDaoImpl();
            this.routeCalculationParameterDao = new RouteCalculationParameterDaoImpl();
            this.yrWeatherDataConnector = new YrWeatherDataConnector();
            this.dataController = new DataController();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Route calculateRoute(double startLat, double startLon, double destinationLat, double destinationLon, Date date) throws Exception {
        NodeTO startRoadNode = nodeDao.getNodeByCoordinates(startLat, startLon);
        NodeTO destinationRoadNode = nodeDao.getNodeByCoordinates(destinationLat, destinationLon);
        NodeTO startNode = null, destinationNode = null;

        if (startRoadNode == null || destinationRoadNode == null)
            throw new DALException("Unknown nodes.");

        if (!nodeDao.isNodeOnRoad(startRoadNode.getNodeId())) {
            startNode = startRoadNode;
            startRoadNode = nodeDao.getClosestNodeOnRoad(startRoadNode.getStreetName(), startLat, startLon);
        }

        if (!nodeDao.isNodeOnRoad(destinationRoadNode.getNodeId())) {
            destinationNode = destinationRoadNode;
            destinationRoadNode = nodeDao.getClosestNodeOnRoad(destinationRoadNode.getStreetName(), destinationLat, destinationLon);
        }

        YrWeatherDataXml yrWeatherDataXml = yrWeatherDataConnector.getWeatherData();
        double travelTimeReductionFactor = getTravelTimeReductionFactor(yrWeatherDataXml, date);
        Weather weatherInfo = getWeatherInfo(yrWeatherDataXml, date);

        FlagEncoder encoder = new CarFlagEncoder();
        GraphHopperStorage graph = dataController.loadMapAsGraph(encoder, travelTimeReductionFactor);

        TraversalMode tMode = TraversalMode.NODE_BASED;
        Dijkstra dijkstra = new Dijkstra(graph, new ShortestWeighting(encoder), tMode);

        Path path = dijkstra.calcPath(startRoadNode.getNodeId(), destinationRoadNode.getNodeId());



        return mapPathToRoute(path, startNode, destinationNode, weatherInfo, travelTimeReductionFactor);
    }

    private Route mapPathToRoute(Path path, NodeTO startNodeTO, NodeTO destinationNodeTO, Weather weatherInfo, double travelTimeReductionFactor) throws DALException {
        List<Node> nodeList = new ArrayList<>();
        List<EdgeIteratorState> edges = path.calcEdges();
        int duration = 0;
        int baseDuration = 0;
        int distance = 0;

        for (int i = 0; i < edges.size(); i++) {
            int baseId = edges.get(i).getBaseNode();
            int adjId = edges.get(i).getAdjNode();
            duration += edges.get(i).getDistance();

            NodeTO baseNodeTO = nodeDao.getNode(baseId);
            NodeTO adjNodeTO = nodeDao.getNode(adjId);

            double tempDistance = TravelTimeCalculationUtil.calcDist(baseNodeTO, adjNodeTO);
            distance += tempDistance;
            baseDuration += TravelTimeCalculationUtil.calcTimeFromDist(tempDistance, nodeDao.getNodeSpeedLimit(baseId));

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

        if (startNode != null) {
            nodeList.add(0, startNode);
        }

        if (destinationNode != null) {
            nodeList.add(destinationNode);
        }

        return new Route(nodeList, duration, baseDuration, distance, weatherInfo, travelTimeReductionFactor);
    }

    private double getTravelTimeReductionFactor(YrWeatherDataXml yrWeatherDataXml, Date date) throws DALException {
        double travelTimeReductionFactor = 1;

        TimeElement timeElement = getTimeElement(yrWeatherDataXml, date);

        if (timeElement != null) {
            double precipitationValue = timeElement.getPrecipitationElement().getPrecipitationValue();
            RouteCalculationParameterTO routeCalculationParameterTO =
                    routeCalculationParameterDao.getByCategoryAndBoundaries(ParameterCategoryValues.PRECIPITATION, precipitationValue);
            travelTimeReductionFactor += routeCalculationParameterTO != null ? routeCalculationParameterTO.getImpactFactor() : 0;

            double temperatureValue = timeElement.getTemperatureElement().getValue();
            routeCalculationParameterTO = routeCalculationParameterDao.getByCategoryAndBoundaries(ParameterCategoryValues.TEMPERATURE, temperatureValue);
            travelTimeReductionFactor += routeCalculationParameterTO != null ? routeCalculationParameterTO.getImpactFactor() : 0;

            double windValue = timeElement.getWindSpeedElement().getMps();
            routeCalculationParameterTO = routeCalculationParameterDao.getByCategoryAndBoundaries(ParameterCategoryValues.WIND, windValue);
            travelTimeReductionFactor += routeCalculationParameterTO != null ? routeCalculationParameterTO.getImpactFactor() : 0;

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            double timeOfDay = calendar.get(Calendar.HOUR_OF_DAY);
            routeCalculationParameterTO = routeCalculationParameterDao.getByCategoryAndBoundaries(ParameterCategoryValues.TRAFFIC, timeOfDay);
            travelTimeReductionFactor += routeCalculationParameterTO != null ? routeCalculationParameterTO.getImpactFactor() : 0;
        }

        return travelTimeReductionFactor;
    }

    private TimeElement getTimeElement(YrWeatherDataXml yrWeatherDataXml, Date dateTime) {
        for (TimeElement timeElement : yrWeatherDataXml.getForecastElement().getTabularElement().getTimeElements()) {
            if (timeElement.getFrom().before(dateTime) && timeElement.getTo().after(dateTime) || timeElement.getTo().equals(dateTime)) {
                return timeElement;
            }
        }

        return null;
    }

    private Weather getWeatherInfo(YrWeatherDataXml yrWeatherDataXml, Date date){
        TimeElement timeElement = getTimeElement(yrWeatherDataXml, date);
        Weather weatherInfo = new Weather(-1.0,-1.0,0.0,"null");
        if(timeElement != null) {
            double precipitationValue = timeElement.getPrecipitationElement().getPrecipitationValue();
            double temperatureValue = timeElement.getTemperatureElement().getValue();
            double windValue = timeElement.getWindSpeedElement().getMps();
            String windDirection = timeElement.getWindDirectionElement().getCode();

            weatherInfo = new Weather(windValue, precipitationValue, temperatureValue, windDirection);
        }
        return weatherInfo;
    }
}
