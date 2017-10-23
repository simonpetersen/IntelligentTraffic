package integration.xml;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement (name = "weatherdata")
public class YrWeatherDataXml {

    private LocationElement locationElement;
    private ForecastElement forecastElement;

    public LocationElement getLocationElement() {
        return locationElement;
    }

    @XmlElement(name = "location")
    public void setLocationElement(LocationElement locationElement) {
        this.locationElement = locationElement;
    }

    public ForecastElement getForecastElement() {
        return forecastElement;
    }

    @XmlElement(name = "forecast")
    public void setForecastElement(ForecastElement forecastElement) {
        this.forecastElement = forecastElement;
    }
}
