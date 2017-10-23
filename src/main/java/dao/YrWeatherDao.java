package dao;

import model.dto.WeatherDataCacheTO;

public interface YrWeatherDao {

    WeatherDataCacheTO getNewestCachedData();
    void cacheWeatherData(WeatherDataCacheTO weatherDataCacheTO);
    void deleteWeatherData(WeatherDataCacheTO weatherDataCacheTO);
}
