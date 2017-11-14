package dao.impl;

import dao.ConnectionFactory;
import dao.YrWeatherDao;
import model.dto.WeatherDataCacheTO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class YrWeatherDaoImpl implements YrWeatherDao {

    private PreparedStatement createPreparedStmt = null;
    private PreparedStatement getNewestPreparedStmt = null;
    private PreparedStatement deletePreparedStmt = null;
    private SimpleDateFormat simpleDateFormat;

    public YrWeatherDaoImpl() throws Exception {

        // Initialization of prepared statements
        // (`Id`,`Type`,`Latitude`,`Longitude`)
        createPreparedStmt = ConnectionFactory.getConnection()
                .prepareStatement("INSERT INTO YrWeatherDataCache (date, xml) VALUES (?,?)");
        getNewestPreparedStmt = ConnectionFactory.getConnection()
                .prepareStatement("SELECT * FROM YrWeatherDataCache ORDER BY date DESC");
        deletePreparedStmt = ConnectionFactory.getConnection()
                .prepareStatement("DELETE FROM YrWeatherDataCache WHERE Id = ?");

        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }

    @Override
    public WeatherDataCacheTO getNewestCachedData() {
        try {
            ResultSet resultSet = getNewestPreparedStmt.executeQuery();
            if (resultSet.first()) {
                String dateString = resultSet.getString("date");
                Date date = simpleDateFormat.parse(dateString);
                String xml = resultSet.getString("Xml");
                int id = resultSet.getInt("Id");

                return new WeatherDataCacheTO(id, date, xml);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void cacheWeatherData(WeatherDataCacheTO weatherDataCacheTO) {
        try {
            String dateTime = simpleDateFormat.format(weatherDataCacheTO.getDate());
            createPreparedStmt.setString(1, dateTime);
            createPreparedStmt.setString(2, weatherDataCacheTO.getXml());

            createPreparedStmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteWeatherData(WeatherDataCacheTO weatherDataCacheTO) {
        try {
            deletePreparedStmt.setInt(1, weatherDataCacheTO.getYrId());

            deletePreparedStmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
