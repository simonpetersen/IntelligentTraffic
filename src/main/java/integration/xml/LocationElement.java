package integration.xml;

import javax.xml.bind.annotation.XmlElement;

public class LocationElement {

    private String name, type, country;

    public String getName() {
        return name;
    }

    @XmlElement
    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    @XmlElement
    public void setType(String type) {
        this.type = type;
    }

    public String getCountry() {
        return country;
    }

    @XmlElement
    public void setCountry(String country) {
        this.country = country;
    }

}
