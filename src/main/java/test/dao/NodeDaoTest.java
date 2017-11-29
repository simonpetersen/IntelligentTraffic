package test.dao;

import dao.NodeDao;
import dao.impl.NodeDaoImpl;
import exception.DALException;
import model.dto.NodeTO;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class NodeDaoTest {

    private NodeDao nodeDao;

    @Before
    public void setUp() throws Exception {
        nodeDao = new NodeDaoImpl();
    }

    @Test
    public void testGetNodeByCoordinates() throws DALException {
        double latitude = 54.7808636;
        double longitude = 11.4887596;

        NodeTO node = nodeDao.getNodeByCoordinates(latitude, longitude);

        assertNotNull(node);

        int expectedId = 1;
        int actualId = node.getNodeId();
        assertEquals(expectedId, actualId);
    }
}
