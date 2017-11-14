package dataload;

import dao.NodeDao;
import dao.RoadDao;
import dao.RoadNodesDao;
import dao.impl.NodeDaoImpl;
import dao.impl.RoadDaoImpl;

import dao.impl.RoadNodesDaoImpl;
import model.dto.NodeTO;
import model.dto.RoadNodesTO;
import model.dto.RoadTO;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataLoadController {

    private NodeDao nodeDao;
    private RoadDao roadDao;
    private RoadNodesDao roadNodesDao;

    private String filePath;
    private Map<Long, Integer> nodeIdMap;
    private Map<Long, Integer> roadIdMap;

    public DataLoadController() {
        try {
            filePath = "src/main/resources/map.osm";
            nodeDao = new NodeDaoImpl();
            roadDao = new RoadDaoImpl();
            roadNodesDao = new RoadNodesDaoImpl();
            nodeIdMap = new HashMap<>();
            roadIdMap = new HashMap<>();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void putMapInDB() throws Exception {
        Document document = loadDocument();
        NodeList nodeList = document.getFirstChild().getChildNodes();

        loadObjects(nodeList);
    }

    private Document loadDocument() throws Exception {
        File fXmlFile = new File(filePath);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document document = dBuilder.parse(fXmlFile);

        return document;
    }

    private void loadObjects(NodeList nodeList) {
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeName().equals("node")) {
                // Persist new Node-object
                loadNode(node);
            } else if (node.getNodeName().equals("way")) {
                loadRoad(node);
            }
        }
    }

    private void loadNode(Node node) {
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

    private void loadRoad(Node node) {
        RoadTO roadTO = new RoadTO(0, 0, 0, 0);
        NodeList childNodes = node.getChildNodes();
        NamedNodeMap namedNodeMap = node.getAttributes();
        Node idNode = namedNodeMap.getNamedItem("id");

        if (idNode != null) {
            long idLong = Long.parseLong(idNode.getNodeValue());
            if (!roadIdMap.containsKey(idLong)) {
                roadIdMap.put(idLong, roadIdMap.size() + 1);
            }

            int id = roadIdMap.get(idLong);
            roadTO.setRoadId(id);
        }

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
                            RoadNodesTO roadNodesTO = new RoadNodesTO(0, roadTO.getRoadId(), refId, sequence);
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
