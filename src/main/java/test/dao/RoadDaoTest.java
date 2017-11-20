package test.dao;

import dao.RoadDao;
import dao.impl.RoadDaoImpl;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertTrue;

public class RoadDaoTest {

    private RoadDao roadDao;

    @Before
    public void setUp() throws Exception {
        roadDao = new RoadDaoImpl();
    }

    @Test
    public void testGetAllRoadIds() throws Exception {
        List<Integer> roadIds = roadDao.getAllRoadIds();

        assertTrue(!roadIds.isEmpty());
    }
}