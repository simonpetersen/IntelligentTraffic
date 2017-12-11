package test.dataload;

import controller.DataController;
import org.junit.Test;
import util.TravelTimeCalculationUtil;

import static org.junit.Assert.assertEquals;
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

    @Test
    public void testCalculateDistance() {
        double latitudePoint1 = 54.7746152;
        double longitudePoint1 = 11.5020717;
        double latitudePoint2 = 54.7747045;
        double longitudePoint2 = 11.5030908;

        // Calculated the distance between the first two nodes of Østergade.
        double calculatedDistance = TravelTimeCalculationUtil.calcDist(latitudePoint1, longitudePoint1, latitudePoint2, longitudePoint2);
        double expectedDistance = 66.11; // meters

        assertEquals(expectedDistance, calculatedDistance, 0.01);
    }
}
