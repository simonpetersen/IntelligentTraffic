package test.integration;

import integration.YrWeatherDataConnector;
import integration.xml.*;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertFalse;

public class YrWeatherDataConnectorTest {

    private YrWeatherDataConnector yrWeatherDataConnector;

    @Before
    public void setUp() throws Exception {
        yrWeatherDataConnector = new YrWeatherDataConnector();
    }

    @Test
    public void getWeatherData() throws Exception {
        YrWeatherDataXml yrWeatherDataXml = yrWeatherDataConnector.getWeatherData();
        ForecastElement forecastElement = yrWeatherDataXml.getForecastElement();
        LocationElement locationElement = yrWeatherDataXml.getLocationElement();
        TabularElement tabularElement = forecastElement.getTabularElement();

        assertNotNull(yrWeatherDataXml);
        assertNotNull(forecastElement);
        assertNotNull(locationElement);

        assertNotNull(tabularElement);
        assertNotNull(tabularElement.getTimeElements());

        boolean isTimeElementsListEmpty = tabularElement.getTimeElements().isEmpty();
        assertFalse(isTimeElementsListEmpty);

        TimeElement timeElement = tabularElement.getTimeElements().get(0);
        assertNotNull(timeElement.getPrecipitationElement());
        assertNotNull(timeElement.getWindDirectionElement());
        assertNotNull(timeElement.getWindSpeedElement());
        assertNotNull(timeElement.getTemperatureElement());
        assertNotNull(timeElement.getPressureElement());
        assertNotNull(timeElement.getFrom());
        assertNotNull(timeElement.getTo());
    }

}