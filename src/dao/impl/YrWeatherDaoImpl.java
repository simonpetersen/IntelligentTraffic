package dao.impl;

import dao.ConnectionFactory;
import dao.YrWeatherDao;
import model.WeatherDataCacheTO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class YrWeatherDaoImpl implements YrWeatherDao {

    private PreparedStatement insertPreparedStmt = null;
    private PreparedStatement getLatestPreparedStmt = null;
    private PreparedStatement deletePreparedStmt = null;
    private SimpleDateFormat simpleDateFormat;

    public YrWeatherDaoImpl() throws Exception {

        // Initialization of prepared statements
        // (`Id`,`Type`,`Latitude`,`Longitude`)
        insertPreparedStmt = ConnectionFactory.getConnection()
                .prepareStatement("INSERT INTO WeatherDataCache (date, xml) VALUES (?,?)");
        getLatestPreparedStmt = ConnectionFactory.getConnection()
                .prepareStatement("SELECT * FROM WeatherDataCache ORDER BY date DESC");
        deletePreparedStmt = ConnectionFactory.getConnection()
                .prepareStatement("DELETE FROM WeatherDataCache WHERE Id = ?");

        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }

    @Override
    public WeatherDataCacheTO getNewestCachedData() {
        try {
            ResultSet resultSet = getLatestPreparedStmt.executeQuery();
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
            insertPreparedStmt.setString(1, dateTime);
            insertPreparedStmt.setString(2, weatherDataCacheTO.getXml());

            insertPreparedStmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteWeatherData(WeatherDataCacheTO weatherDataCacheTO) {
        try {
            deletePreparedStmt.setInt(1, weatherDataCacheTO.getId());

            deletePreparedStmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
