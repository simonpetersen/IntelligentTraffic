package dao.impl;

import dao.ConnectionFactory;
import dao.UserDao;
import model.dto.UserTO;
import util.KeyGenerator;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

public class UserDaoImpl implements UserDao {

    private PreparedStatement createUserStmt, loginStmt, deleteUserStmt;

    public UserDaoImpl() throws Exception {
        createUserStmt = ConnectionFactory.getConnection().prepareStatement("INSERT INTO USER (username, password, apikey, name, admin)" +
                "VALUES (?,SHA1(?),?,?,?)");
        deleteUserStmt = ConnectionFactory.getConnection().prepareStatement("DELETE FROM USER WHERE username = ?");
        loginStmt = ConnectionFactory.getConnection().prepareStatement("SELECT * FROM USER WHERE username = ? AND password = SHA1(?)");
    }

    @Override
    public void createUser(UserTO user) throws Exception {
        createUserStmt.setString(1, user.getUsername());
        createUserStmt.setString(2, user.getPassword());
        createUserStmt.setString(4, user.getName());
        createUserStmt.setBoolean(5, user.isAdmin());

        String key = KeyGenerator.generateApiKey();
        createUserStmt.setString(3, key);

        try {
            createUserStmt.executeUpdate();
        } catch (SQLIntegrityConstraintViolationException e) {
            // Creation failed because of non-unique api key. Retry with new key.
            String newKey = KeyGenerator.generateApiKey();
            System.out.println(newKey);
            createUserStmt.setString(3, newKey);
            createUserStmt.executeUpdate();
        }
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

            return new UserTO(username, null, apiKey, name, admin);
        }

        return null;
    }

    @Override
    public void deleteUser(String username) throws Exception {
        deleteUserStmt.setString(1, username);

        deleteUserStmt.executeUpdate();
    }
}
