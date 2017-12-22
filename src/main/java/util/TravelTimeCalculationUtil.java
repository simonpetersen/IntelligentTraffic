package util;

import model.dto.NodeTO;

/**
 * Helper method for travel time and distance calculation
 */
public class TravelTimeCalculationUtil {

    public static double calcDist(NodeTO node1, NodeTO node2){
        //This function calculates distance between two coordinates in meters. For kilometers, divide R with 1000.
        final double Latitude1 = node1.getLatitude();
        final double Longitude1 = node1.getLongitude();
        final double Latitude2 = node2.getLatitude();
        final double Longitude2 = node2.getLongitude();

        final double R = 6371000.0;
        final double dLat = Math.toRadians(Latitude1 - Latitude2);
        final double dLon = Math.toRadians(Longitude1 - Longitude2);

        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(Latitude1)) * Math.cos(Math.toRadians(Latitude2)) *
                        Math.sin(dLon/2) * Math.sin(dLon/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

        return R * c;
    }

    public static double calcTime(NodeTO node1, NodeTO node2, int MaxSpeed){
        //Distance calculated in kilometers
        double dist = calcDist(node1, node2)/1000;
        //Travel Time in seconds
        double time = dist / MaxSpeed * 3600;

        return time;
    }

    public static double calcTimeFromDist(double distance, int MaxSpeed){
        return (distance / 1000) / (MaxSpeed * 3600);
    }
}
