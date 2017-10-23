package integration.xml;

import javax.xml.bind.annotation.XmlAttribute;

public class WindSpeedElement {

    private double mps;
    private String name;

    public double getMps() {
        return mps;
    }

    @XmlAttribute
    public void setMps(double mps) {
        this.mps = mps;
    }

    public String getName() {
        return name;
    }

    @XmlAttribute
    public void setName(String name) {
        this.name = name;
    }
}
