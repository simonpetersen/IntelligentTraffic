package test.dao;

import dao.YrWeatherDao;
import dao.impl.YrWeatherDaoImpl;
import model.WeatherDataCacheTO;
import org.junit.Before;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class YrWeatherDaoTest {

    private YrWeatherDao yrWeatherDao;
    private SimpleDateFormat simpleDateFormat;

    @Before
    public void setUp() throws Exception {
        yrWeatherDao = new YrWeatherDaoImpl();
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }

    @Test
    public void getLatestCachedWeatherData() throws Exception {
        String xml = "Test";
        Date date = new Date();
        WeatherDataCacheTO weatherDataCacheTO = new WeatherDataCacheTO(1, date, xml);
        // Insert new row
        yrWeatherDao.cacheWeatherData(weatherDataCacheTO);

        WeatherDataCacheTO cachedWeatherData = yrWeatherDao.getNewestCachedData();
        assertNotNull(cachedWeatherData);

        String expected = simpleDateFormat.format(date);
        String actual = simpleDateFormat.format(cachedWeatherData.getDate());
        assertEquals(expected, actual);

        // Delete test row
        yrWeatherDao.deleteWeatherData(cachedWeatherData);
    }
}
