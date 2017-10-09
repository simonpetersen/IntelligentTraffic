package integration.xml;

import javax.xml.bind.annotation.XmlElement;
import java.util.List;

public class TabularElement {

    private List<TimeElement> timeElements;

    public List<TimeElement> getTimeElements() {
        return timeElements;
    }

    @XmlElement(name = "time")
    public void setTimeElements(List<TimeElement> timeElements) {
        this.timeElements = timeElements;
    }

}
