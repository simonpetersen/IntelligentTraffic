package integration;

import dao.YrWeatherDao;
import dao.impl.YrWeatherDaoImpl;
import exception.DALException;
import integration.xml.YrWeatherDataXml;
import model.dto.WeatherDataCacheTO;
import util.XmlHelper;

import javax.xml.bind.JAXBException;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;
import java.util.stream.Stream;

public class YrWeatherDataConnector {

    private String yrMariboURL;
    private YrWeatherDao yrWeatherDao;

    // Ten minutes in milliseconds: 10 x 60 x 1000
    private final long tenMinutes = 600000;

    public YrWeatherDataConnector() {
        yrMariboURL = "http://www.yr.no/place/Denmark/Zealand/Maribo/forecast.xml";

        try {
            yrWeatherDao = new YrWeatherDaoImpl();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public YrWeatherDataXml getWeatherData() throws Exception {
        WeatherDataCacheTO weatherDataCacheTO = yrWeatherDao.getNewestCachedData();

        long cachedTime = new Date().getTime() - weatherDataCacheTO.getDate().getTime();
        if (cachedTime < tenMinutes) {
            String xml = weatherDataCacheTO.getXml();
            return XmlHelper.stringToObj(xml, YrWeatherDataXml.class);
        }

        // Cached weather data is too old, retrieve new and cache
        YrWeatherDataXml yrWeatherDataXml = getWeatherDataFromYr();
        Date date = new Date();
        String xml = XmlHelper.objToString(yrWeatherDataXml);
        WeatherDataCacheTO weatherData = new WeatherDataCacheTO(1, date, xml);
        yrWeatherDao.cacheWeatherData(weatherData);

        return yrWeatherDataXml;
    }

    private YrWeatherDataXml getWeatherDataFromYr() {
        try {
            URL url = new URL(yrMariboURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            if (connection.getResponseCode() == 200) {
                try (InputStreamReader streamReader = new InputStreamReader(connection.getInputStream());
                     BufferedReader br = new BufferedReader(streamReader);
                     Stream<String> lines = br.lines()) {
                    String xmlObj = "";
                    for (Object obj : lines.toArray()) {
                        String s = (String) obj;
                        xmlObj.concat(s);
                    }
                    return XmlHelper.stringToObj(xmlObj, YrWeatherDataXml.class);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
