package dao;

import exception.DALException;
import model.dto.WeatherDataCacheTO;

public interface YrWeatherDao {

    WeatherDataCacheTO getNewestCachedData() throws DALException;
    void cacheWeatherData(WeatherDataCacheTO weatherDataCacheTO) throws DALException;
    void deleteWeatherData(WeatherDataCacheTO weatherDataCacheTO) throws DALException;
}
