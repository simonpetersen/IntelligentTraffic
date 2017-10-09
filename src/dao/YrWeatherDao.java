package dao;

import model.WeatherDataCacheTO;
import integration.xml.YrWeatherDataXml;

public interface YrWeatherDao {

    WeatherDataCacheTO getNewestCachedData();
    void cacheWeatherData(WeatherDataCacheTO weatherDataCacheTO);
    void deleteWeatherData(WeatherDataCacheTO weatherDataCacheTO);
}
