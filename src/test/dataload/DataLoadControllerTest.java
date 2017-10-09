package test.dataload;

import dataload.DataLoadController;
import org.junit.Test;

import static org.junit.Assert.fail;

public class DataLoadControllerTest {

    private DataLoadController dataLoadController;

    @Test
    public void loadData() {
        try {
            dataLoadController = new DataLoadController();
            dataLoadController.loadData();
        } catch (Exception e) {
            fail();
        }
    }
}
