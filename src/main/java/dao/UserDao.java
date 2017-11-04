package dao;

import model.dto.UserTO;

import java.sql.SQLException;

public interface UserDao {

    void createUser(UserTO user) throws Exception;
    UserTO login(String username, String password) throws Exception;
    void deleteUser(String username) throws Exception;
}
