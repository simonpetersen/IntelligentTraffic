package integration.xml;

import javax.xml.bind.annotation.XmlAttribute;

public class WindDirectionElement {

    private double degree;
    private String code, name;

    public double getDegree() {
        return degree;
    }

    @XmlAttribute(name = "deg")
    public void setDegree(double degree) {
        this.degree = degree;
    }

    public String getCode() {
        return code;
    }

    @XmlAttribute
    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    @XmlAttribute
    public void setName(String name) {
        this.name = name;
    }
}
