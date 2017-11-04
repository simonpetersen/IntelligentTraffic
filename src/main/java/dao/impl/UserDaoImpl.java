package dao.impl;

import dao.ConnectionFactory;
import dao.UserDao;
import model.dto.UserTO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDaoImpl implements UserDao {

    private PreparedStatement createUserStmt, loginStmt, deleteUserStmt;

    public UserDaoImpl() throws Exception {
        createUserStmt = ConnectionFactory.getConnection().prepareStatement("INSERT INTO USER (username, password, apikey, name, admin)" +
                "VALUES (?,?,?,?,?)");
        deleteUserStmt = ConnectionFactory.getConnection().prepareStatement("DELETE FROM USER WHERE username = ?");
        loginStmt = ConnectionFactory.getConnection().prepareStatement("SELECT * FROM USER WHERE username = ? AND password = SHA1(?)");
    }

    @Override
    public void createUser(UserTO user) {

    }

    @Override
    public UserTO login(String username, String password) throws Exception {
        loginStmt.setString(1, username);
        loginStmt.setString(2, password);

        ResultSet resultSet = loginStmt.executeQuery();

        if (resultSet.first()) {
            String apiKey = resultSet.getString("apiKey");
            String name = resultSet.getString("name");
            boolean admin = resultSet.getBoolean("admin");

            return new UserTO(username, password, apiKey, name, admin);
        }

        return null;
    }

    @Override
    public void deleteUser(String username) {

    }
}
