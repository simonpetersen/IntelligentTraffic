package controller;

import dao.UserDao;
import dao.impl.UserDaoImpl;
import exception.DALException;
import exception.WebServiceException;
import model.dto.UserTO;
import util.KeyGenerator;

import javax.ws.rs.core.Response;

public class UserController {

    private UserDao userDao;

    public UserController() {
        try {
            userDao = new UserDaoImpl();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public UserTO login(String username, String password) throws WebServiceException {
        if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
            throw new WebServiceException("Username or password can't be empty.");
        }

        UserTO user = null;
        try {
            user = userDao.login(username, password);
        } catch (DALException e) {
            throw new WebServiceException("Error occured with login: " + e.getMessage(), e);
        }

        if (user == null) {
            throw new WebServiceException("Unknown username or password.");
        }

        return user;
    }

    public Response createUser(String username, String password, String name, String adminStr, String apiKey) throws WebServiceException {
        if (!userDao.validateApiKeyAdmin(apiKey)) {
            throw new WebServiceException("Authorization error: User is not authorized for this operation.");
        }

        if (username != null && password != null && name != null && adminStr != null && !username.isEmpty() &&
                !password.isEmpty() && !name.isEmpty() && !adminStr.isEmpty()) {
            boolean isAdmin = Boolean.parseBoolean(adminStr);
            String generatedKey = KeyGenerator.generateApiKey();

            UserTO user = new UserTO(username, password, generatedKey, name, isAdmin);

            try {
                userDao.createUser(user);
            } catch (DALException e) {
                throw new WebServiceException("Error while creating user: " + e.getMessage());
            }

            return Response.ok().build();
        }

        throw new WebServiceException("Invalid data. User creation failed.");
    }

    public Response deleteUser(String username, String apiKey) throws WebServiceException {
        if (!userDao.validateApiKeyAdmin(apiKey)) {
            throw new WebServiceException("Authorization error: User is not authorized for this operation.");
        }

        try {
            userDao.deleteUser(username);
        } catch (DALException e) {
            throw new WebServiceException("Error while deleting user: " + e.getMessage());
        }

        return Response.ok().build();
    }
}