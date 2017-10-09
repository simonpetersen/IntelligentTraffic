package integration.xml;

import javax.xml.bind.annotation.XmlAttribute;

public class PrecipitationElement {

    private double precipitationValue, precipitationMin, precipitationMax;

    public double getPrecipitationValue() {
        return precipitationValue;
    }

    @XmlAttribute(name = "value")
    public void setPrecipitationValue(double precipitationValue) {
        this.precipitationValue = precipitationValue;
    }

    public double getPrecipitationMin() {
        return precipitationMin;
    }

    @XmlAttribute(name = "minvalue")
    public void setPrecipitationMin(double precipitationMin) {
        this.precipitationMin = precipitationMin;
    }

    public double getPrecipitationMax() {
        return precipitationMax;
    }

    @XmlAttribute(name = "maxvalue")
    public void setPrecipitationMax(double precipitationMax) {
        this.precipitationMax = precipitationMax;
    }
}
