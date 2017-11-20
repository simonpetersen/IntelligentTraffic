package test.dataload;

import controller.DataController;
import org.junit.Test;

import static org.junit.Assert.fail;

public class DataControllerTest {

    private DataController dataController;

    @Test
    public void loadData() {
        try {
            dataController = new DataController();
            dataController.saveMapInDB();
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
}
