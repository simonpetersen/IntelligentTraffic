package integration.xml;

import javax.xml.bind.annotation.XmlAttribute;

public class PressureElement {

    private double value;
    private String unit;

    public double getValue() {
        return value;
    }

    @XmlAttribute
    public void setValue(double value) {
        this.value = value;
    }

    public String getUnit() {
        return unit;
    }

    @XmlAttribute
    public void setUnit(String unit) {
        this.unit = unit;
    }
}
