package model;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement
//@XmlAccessorType(XmlAccessType.FIELD)
public class Route {

    private List<Node> node;
    private int duration, baseDuration, distance;

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
}
