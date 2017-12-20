package controller;

import com.graphhopper.routing.util.EncodingManager;
import com.graphhopper.routing.util.FlagEncoder;
import com.graphhopper.storage.GraphBuilder;
import com.graphhopper.storage.GraphHopperStorage;
import dao.NodeDao;
import dao.RoadDao;
import dao.RoadNodesDao;
import dao.impl.NodeDaoImpl;
import dao.impl.RoadDaoImpl;

import dao.impl.RoadNodesDaoImpl;
import exception.DALException;
import model.dto.NodeTO;
import model.dto.RoadNodesTO;
import model.dto.RoadTO;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import util.TravelTimeCalculationUtil;

import javax.ws.rs.core.Application;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataController {

    private NodeDao nodeDao;
    private RoadDao roadDao;
    private RoadNodesDao roadNodesDao;

    private String filePath;
    private Map<Long, Integer> nodeIdMap;

    public DataController() {
        try {
            filePath = "map.osm";
            nodeDao = new NodeDaoImpl();
            roadDao = new RoadDaoImpl();
            roadNodesDao = new RoadNodesDaoImpl();
            nodeIdMap = new HashMap<>();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public GraphHopperStorage loadMapAsGraph(FlagEncoder encoder, double travelTimeReductionFactor) throws DALException {
        EncodingManager em = new EncodingManager(encoder);
        GraphBuilder gb = new GraphBuilder(em).setLocation("graphhopper_folder").setStore(true);

        GraphHopperStorage graph;
        try {
            graph = gb.load();

            return graph;
        } catch (IllegalStateException e) {
            graph = gb.create();
            loadNodes(graph);
            loadRoads(graph, travelTimeReductionFactor);
            //graph.flush();

            return graph;
        }
    }

    private void loadNodes(GraphHopperStorage graph) throws DALException {
        List<Integer> nodeIds = nodeDao.getAllNodeIds();

        for (int id : nodeIds) {
            NodeTO node = nodeDao.getNode(id);
            graph.getNodeAccess().setNode(id, node.getLatitude(), node.getLongitude());
        }
    }

    private void loadRoads(GraphHopperStorage graph, double travelTimeReductionFactor) throws DALException {
        List<RoadTO> roads = roadDao.getAllRoads();
        for (RoadTO road : roads) {
            List<Integer> roadNodesIds = roadNodesDao.getRoadNodesIds(road.getRoadId());
            for (int i = 0; i < roadNodesIds.size() - 1; i++) {
                int startNodeId = roadNodesDao.getRoadNodes(roadNodesIds.get(i)).getNodeId();
                int endNodeId = roadNodesDao.getRoadNodes(roadNodesIds.get(i+1)).getNodeId();
                NodeTO startNode = nodeDao.getNode(startNodeId);
                NodeTO endNode = nodeDao.getNode(endNodeId);

                int maxSpeed = road.getMaxSpeed() != 0 ? road.getMaxSpeed() : 50;
                double time = TravelTimeCalculationUtil.calcTime(startNode, endNode, maxSpeed);
                time *= travelTimeReductionFactor;

                graph.edge(startNode.getNodeId(), endNode.getNodeId(), time, !road.isOneWay());
            }
        }
    }

    public void saveMapInDB() throws Exception {
        Document document = loadOsmDocument();
        NodeList nodeList = document.getFirstChild().getChildNodes();

        persistObjects(nodeList);
    }

    private Document loadOsmDocument() throws Exception {
        //File fXmlFile = new File(filePath);
        InputStream is = Application.class.getClassLoader().getResourceAsStream(filePath);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        //Document document = dBuilder.parse(fXmlFile);
        Document document = dBuilder.parse(is);

        return document;
    }

    private void persistObjects(NodeList nodeList) throws DALException {
        int roadId = 1;
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeName().equals("node")) {
                // Persist new Node-object
                persistNode(node);
            } else if (node.getNodeName().equals("way")) {
                persistRoad(node, roadId++);
            }
        }
    }

    private void persistNode(Node node) throws DALException {
        NamedNodeMap namedNodeMap = node.getAttributes();
        Node idNode = namedNodeMap.getNamedItem("id");
        Node lonNode = namedNodeMap.getNamedItem("lon");
        Node latNode = namedNodeMap.getNamedItem("lat");

        if (idNode != null && lonNode != null && latNode != null) {
            long idLong = Long.parseLong(idNode.getNodeValue());
            if (!nodeIdMap.containsKey(idLong)) {
                nodeIdMap.put(idLong, nodeIdMap.size() + 1);
            }
            double latitude = Double.parseDouble(latNode.getNodeValue());
            double longitude = Double.parseDouble(lonNode.getNodeValue());
            int id = nodeIdMap.get(idLong);
            String streetname = getNodeStreetName(node.getChildNodes());

            NodeTO nodeTO = new NodeTO(id, null, latitude, longitude, streetname);
            nodeDao.insertNode(nodeTO);
        }
    }

    private void persistRoad(Node node, int roadId) throws DALException {
        RoadTO roadTO = new RoadTO(roadId, 0, null, false, 0);
        NodeList childNodes = node.getChildNodes();

        if (childNodes != null) {
            int sequence = 0;
            // Iterate over childnodes
            List<RoadNodesTO> roadNodes = new ArrayList<>();
            for (int i = 0; i < childNodes.getLength(); i++) {
                Node childNode = childNodes.item(i);
                NamedNodeMap childNodeAttributes = childNode.getAttributes();
                if (childNodeAttributes != null) {
                    // Look for child-node with attribute k="name" and v=streetname or key="oneway"
                    Node keyAttribute = childNodeAttributes.getNamedItem("k");
                    if (keyAttribute != null) {
                        Node valueAttribute = childNodeAttributes.getNamedItem("v");
                        if (keyAttribute.getNodeValue().equals("name")) {
                            roadTO.setStreetName(valueAttribute.getNodeValue());
                        } else if (keyAttribute.getNodeValue().equals("oneway")) {
                            boolean oneWay = valueAttribute.getNodeValue().equals("yes");
                            roadTO.setOneWay(oneWay);
                        } else if(keyAttribute.getNodeValue().equals("maxspeed")) {
                            int maxSpeed = Integer.parseInt(valueAttribute.getNodeValue());
                            roadTO.setMaxSpeed(maxSpeed);
                        }
                    }

                    // Look for childnode with attribute ref=nodeid
                    Node refNode = childNodeAttributes.getNamedItem("ref");
                    if (refNode != null) {
                        long ref = Long.parseLong(refNode.getNodeValue());
                        if (nodeIdMap.containsKey(ref)) {
                            int refId = nodeIdMap.get(ref);
                            RoadNodesTO roadNodesTO = new RoadNodesTO(0, roadId, refId, sequence);
                            roadNodes.add(roadNodesTO);
                            sequence++;
                        }
                    }
                }
            }

            double distance = calcTotalRoadDistance(roadNodes);
            roadTO.setDistance(distance);

            roadDao.insertRoad(roadTO);

            roadNodesDao.insertRoadNodesList(roadNodes);
        }
    }

    private double calcTotalRoadDistance(List<RoadNodesTO> roadNodes) throws DALException {
        List<Double> distances = new ArrayList<>();

        for (int i = 0; i < roadNodes.size() - 1; i++) {
            NodeTO baseNode = nodeDao.getNode(roadNodes.get(i).getNodeId());
            NodeTO adjNode = nodeDao.getNode(roadNodes.get(i+1).getNodeId());

            distances.add(TravelTimeCalculationUtil.calcDist(baseNode, adjNode));
        }

        return distances.stream().mapToDouble(dist -> dist).sum();
    }

    private String getNodeStreetName(NodeList nodeList) {
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node childNode = nodeList.item(i);
            NamedNodeMap childNodeAttributes = childNode.getAttributes();
            if (childNodeAttributes != null) {
                Node keyAttribute = childNodeAttributes.getNamedItem("k");
                if (keyAttribute != null && keyAttribute.getNodeValue().equals("addr:street")) {
                    Node valueAttribute = childNodeAttributes.getNamedItem("v");
                    return valueAttribute.getNodeValue();
                }
            }
        }
        return null;
    }
}
