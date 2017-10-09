package integration.xml;

import javax.xml.bind.annotation.XmlElement;

public class ForecastElement {

    private TabularElement tabularElement;

    public TabularElement getTabularElement() {
        return tabularElement;
    }

    @XmlElement(name = "tabular")
    public void setTabularElement(TabularElement tabularElement) {
        this.tabularElement = tabularElement;
    }
}
