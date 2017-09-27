package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnectionFactory {

    private static Connection connection;
    private static Statement stmt;

    public static Connection getConnection() throws ClassNotFoundException, SQLException, InstantiationException, IllegalAccessException {
        if (connection == null) {
            configureConnection();
        }

        return connection;
    }

    private static void configureConnection() throws SQLException, IllegalAccessException, ClassNotFoundException, InstantiationException {
        connection = connectToDatabase("jdbc:mysql://" + DbConstants.server + ":" + DbConstants.port + "/" +
                DbConstants.database, DbConstants.username, DbConstants.password);
        stmt = connection.createStatement();
    }

    private static Connection connectToDatabase(String url, String username, String password)
            throws InstantiationException, IllegalAccessException, ClassNotFoundException, SQLException {
        // call the driver class' no argument constructor
        Class.forName("com.mysql.jdbc.Driver").newInstance();

        // get Connection-object via DriverManager
        return (Connection) DriverManager.getConnection(url, username, password);
    }
}
