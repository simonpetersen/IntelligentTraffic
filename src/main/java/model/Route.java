package model;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement
//@XmlAccessorType(XmlAccessType.FIELD)
public class Route {

    private List<Node> node;
    private int duration;

    // Empty constructor required for XML-parsing
    public Route() {}

    public Route(List<Node> nodes, int duration) {
        this.node = nodes;
        this.duration = duration;
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
}
