package dao.impl;

import dao.ConnectionFactory;
import dao.UserDao;
import exception.DALException;
import model.dto.UserTO;
import util.KeyGenerator;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

public class UserDaoImpl implements UserDao {

    private PreparedStatement createUserStmt, loginStmt, deleteUserStmt, validateStmt, validateAdminStmt;

    public UserDaoImpl() throws Exception {
        createUserStmt = ConnectionFactory.getConnection().prepareStatement("INSERT INTO User (Username, Password, ApiKey , Name, Admin)" +
                "VALUES (?,?,?,?,?)");
        deleteUserStmt = ConnectionFactory.getConnection().prepareStatement("DELETE FROM User WHERE Username = ?");
        loginStmt = ConnectionFactory.getConnection().prepareStatement("SELECT * FROM User WHERE username = ? AND password = ?");
        validateStmt = ConnectionFactory.getConnection().prepareStatement("SELECT * FROM User WHERE ApiKey = ?");
        validateAdminStmt = ConnectionFactory.getConnection().prepareStatement("SELECT Admin FROM User WHERE ApiKey = ?");
    }

    @Override
    public void createUser(UserTO user) throws DALException {
        try {
            createUserStmt.setString(1, user.getUsername());
            createUserStmt.setString(2, user.getPassword());
            createUserStmt.setString(3, user.getApiKey());
            createUserStmt.setString(4, user.getName());
            createUserStmt.setBoolean(5, user.isAdmin());

            createUserStmt.executeUpdate();
        } catch (SQLException e) {
            throw new DALException(e.getMessage());
        }

    }

    @Override
    public UserTO login(String username, String password) throws DALException {
        try {
            loginStmt.setString(1, username);
            loginStmt.setString(2, password);

            ResultSet resultSet = loginStmt.executeQuery();

            if (resultSet.first()) {
                String apiKey = resultSet.getString("apiKey");
                String name = resultSet.getString("name");
                boolean admin = resultSet.getBoolean("admin");

                return new UserTO(username, password, apiKey, name, admin);
            }
        } catch (SQLException e) {
            throw new DALException(e.getMessage());
        }

        return null;
    }

    @Override
    public void deleteUser(String username) throws DALException {
        try {
            deleteUserStmt.setString(1, username);

            deleteUserStmt.executeUpdate();
        } catch (SQLException e) {
            throw new DALException(e.getMessage());
        }
    }

    @Override
    public boolean validateApiKey(String apiKey) {
        try {
            validateStmt.setString(1, apiKey);

            ResultSet resultSet = validateStmt.executeQuery();

            if (resultSet.first()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    @Override
    public boolean validateApiKeyAdmin(String apiKey) {
        try {
            validateAdminStmt.setString(1, apiKey);

            ResultSet resultSet = validateAdminStmt.executeQuery();

            if (resultSet.first()) {
                return resultSet.getBoolean("Admin");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }
}
