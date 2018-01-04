package model;


import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement
//@XmlAccessorType(XmlAccessType.FIELD)
public class Route {

    private List<Node> node;
    private int duration, baseDuration, distance;
    private double temperature, precipitation, windSpeed, traveltimeReductionFactor;
    private String windDirection;

    // Empty constructor required for XML-parsing
    public Route() {}

    public Route(List<Node> nodes, int duration) {
        this.node = nodes;
        this.duration = duration;
        this.baseDuration = 0;
        this.distance = 0;
    }

    public Route(List<Node> nodes, int duration, int baseDuration, int distance){
        this.node = nodes;
        this.duration = duration;
        this.baseDuration = baseDuration;
        this.distance = distance;
    }

    public Route(List<Node> nodes, int duration, int baseDuration, int distance, Weather weather, double traveltimeReductionFactor){
        this.node = nodes;
        this.duration = duration;
        this.baseDuration = baseDuration;
        this.distance = distance;
        this.precipitation = weather.getPrecipitation();
        this.temperature = weather.getTemperature();
        this.windSpeed = weather.getWindSpeed();
        this.windDirection = weather.getWindDirection();
        this.traveltimeReductionFactor = traveltimeReductionFactor;
    }

    public List<Node> getNode() {
        return node;
    }

    public void setNode(List<Node> nodes) {
        this.node = nodes;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getBaseDuration(){ return baseDuration; }

    public void setBaseDuration(int baseDuration) {
        this.baseDuration = baseDuration;
    }

    public int getDistance(){ return distance; }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public void setWindDirection(String windDirection) {
        this.windDirection = windDirection;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public void setPrecipitation(double precipitation) {
        this.precipitation = precipitation;
    }

    public void setTraveltimeReductionFactor(double traveltimeReductionFactor) {
        this.traveltimeReductionFactor = traveltimeReductionFactor;
    }

    public String getWindDirection() {
        return windDirection;
    }

    public double getPrecipitation() {
        return precipitation;
    }

    public double getTemperature() {
        return temperature;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public double getTraveltimeReductionFactor() {
        return traveltimeReductionFactor;
    }
}
