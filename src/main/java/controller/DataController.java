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

    public GraphHopperStorage loadMapAsGraph(FlagEncoder encoder) throws DALException {
        EncodingManager em = new EncodingManager(encoder);
        GraphBuilder gb = new GraphBuilder(em).setLocation("graphhopper_folder").setStore(true);

        GraphHopperStorage graph;
        try {
            graph = gb.load();

            return graph;
        } catch (IllegalStateException e) {
            graph = gb.create();
            loadNodes(graph);
            loadRoads(graph);
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

    private void loadRoads(GraphHopperStorage graph) throws DALException {
        List<Integer> roadIds = roadDao.getAllRoadIds();
        for (int roadId : roadIds) {
            List<Integer> roadNodesIds = roadNodesDao.getRoadNodesIds(roadId);
            for (int i = 0; i < roadNodesIds.size() - 1; i++) {
                RoadNodesTO startNode = roadNodesDao.getRoadNodes(roadNodesIds.get(i));
                RoadNodesTO endNode = roadNodesDao.getRoadNodes(roadNodesIds.get(i+1));
                graph.edge(startNode.getNodeId(), endNode.getNodeId(), 1, true);
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

            NodeTO nodeTO = new NodeTO(id, null, latitude, longitude);
            nodeDao.insertNode(nodeTO);
        }
    }

    private void persistRoad(Node node, int roadId) throws DALException {
        RoadTO roadTO = new RoadTO(roadId, 0, 0);
        NodeList childNodes = node.getChildNodes();

        roadDao.insertRoad(roadTO);

        if (childNodes != null) {
            int sequence = 0;
            List<RoadNodesTO> roadNodes = new ArrayList<>();
            for (int i = 0; i < childNodes.getLength(); i++) {
                Node childNode = childNodes.item(i);
                NamedNodeMap childNodeAttributes = childNode.getAttributes();
                if (childNodeAttributes != null) {
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

            roadNodesDao.insertRoadNodesList(roadNodes);
        }
    }
}
