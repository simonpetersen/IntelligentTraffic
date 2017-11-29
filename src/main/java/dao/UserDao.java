package dao;

import exception.DALException;
import model.dto.UserTO;

import java.sql.SQLException;

public interface UserDao {

    void createUser(UserTO user) throws DALException;
    UserTO login(String username, String password) throws DALException;
    void deleteUser(String username) throws DALException;
    boolean validateApiKey(String apiKey) throws DALException;
    boolean validateApiKeyAdmin(String apiKey) throws DALException;
}
