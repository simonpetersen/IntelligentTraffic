package dataload;

import dao.NodeDao;
import dao.RoadDao;
import dao.RoadNodesDao;
import dao.impl.NodeDaoImpl;
import dao.impl.RoadDaoImpl;

import dao.impl.RoadNodesDaoImpl;
import model.NodeTO;
import model.RoadNodesTO;
import model.RoadTO;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataLoadController {

    private NodeDao nodeDao;
    private RoadDao roadDao;
    private RoadNodesDao roadNodesDao;

    private String filePath;

    public DataLoadController() {
        try {
            filePath = "src/map.osm";
            nodeDao = new NodeDaoImpl();
            roadDao = new RoadDaoImpl();
            roadNodesDao = new RoadNodesDaoImpl();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadData() throws Exception {
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
            long id = Long.parseLong(idNode.getNodeValue());
            double latitude = Double.parseDouble(latNode.getNodeValue());
            double longitude = Double.parseDouble(lonNode.getNodeValue());

            NodeTO nodeTO = new NodeTO(id, null, latitude, longitude);
            nodeDao.insertNode(nodeTO);
        }
    }

    private void loadRoad(Node node) {
        RoadTO roadTO = new RoadTO(0L, 0, 0, 0);
        NodeList childNodes = node.getChildNodes();
        NamedNodeMap namedNodeMap = node.getAttributes();
        Node idNode = namedNodeMap.getNamedItem("id");

        if (idNode != null) {
            long id = Long.parseLong(idNode.getNodeValue());
            roadTO.setId(id);
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
                        RoadNodesTO roadNodesTO = new RoadNodesTO(1, roadTO.getId(), ref, sequence);
                        roadNodes.add(roadNodesTO);
                        sequence++;
                    }
                }
            }

            roadNodesDao.insertRoadNodesList(roadNodes);
        }
    }
}
