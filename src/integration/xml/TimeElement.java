package integration.xml;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import java.util.Date;

public class TimeElement {

    private Date from, to;
    private PrecipitationElement precipitationElement;
    private WindDirectionElement windDirectionElement;
    private WindSpeedElement windSpeedElement;
    private TemperatureElement temperatureElement;
    private PressureElement pressureElement;

    public Date getFrom() {
        return from;
    }

    @XmlAttribute
    public void setFrom(Date from) {
        this.from = from;
    }

    public Date getTo() {
        return to;
    }

    @XmlAttribute
    public void setTo(Date to) {
        this.to = to;
    }

    public PrecipitationElement getPrecipitationElement() {
        return precipitationElement;
    }

    @XmlElement(name = "precipitation")
    public void setPrecipitationElement(PrecipitationElement precipitationElement) {
        this.precipitationElement = precipitationElement;
    }

    public WindDirectionElement getWindDirectionElement() {
        return windDirectionElement;
    }

    @XmlElement(name = "windDirection")
    public void setWindDirectionElement(WindDirectionElement windDirectionElement) {
        this.windDirectionElement = windDirectionElement;
    }

    public WindSpeedElement getWindSpeedElement() {
        return windSpeedElement;
    }

    @XmlElement(name = "windSpeed")
    public void setWindSpeedElement(WindSpeedElement windSpeedElement) {
        this.windSpeedElement = windSpeedElement;
    }

    public TemperatureElement getTemperatureElement() {
        return temperatureElement;
    }

    @XmlElement(name = "temperature")
    public void setTemperatureElement(TemperatureElement temperatureElement) {
        this.temperatureElement = temperatureElement;
    }

    public PressureElement getPressureElement() {
        return pressureElement;
    }

    @XmlElement(name = "pressure")
    public void setPressureElement(PressureElement pressureElement) {
        this.pressureElement = pressureElement;
    }
}
