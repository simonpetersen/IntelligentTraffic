package util;

import model.dto.NodeTO;

/**
 * Helper method for travel time and distance calculation
 */
public class TravelTimeCalculationUtil {

    public static double calcDist(double latitude1, double longitude1, double latitude2, double longitude2){
        //This function calculates distance between two coordinates in meters. For kilometers, divide R with 1000.
        double R = 6371000.0;
        double dLat = Math.toRadians(latitude1 - latitude2);
        double dLon = Math.toRadians(longitude1 - longitude2);

        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(latitude1)) * Math.cos(Math.toRadians(latitude2)) *
                        Math.sin(dLon/2) * Math.sin(dLon/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

        return R * c;
    }

    public static double calcTime(NodeTO node1, NodeTO node2, int MaxSpeed){
        //Distance calculated in kilometers
        double dist = calcDist(node1.getLatitude(), node1.getLongitude(), node2.getLatitude(), node2.getLongitude())/1000;
        //Travel Time in seconds
        double time = dist / MaxSpeed * 3600;

        return time;
    }
}
