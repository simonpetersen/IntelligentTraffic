package integration.xml;

import javax.xml.bind.annotation.XmlAttribute;

public class TemperatureElement {

    private int value;
    private String unit;

    public int getValue() {
        return value;
    }

    @XmlAttribute
    public void setValue(int value) {
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
