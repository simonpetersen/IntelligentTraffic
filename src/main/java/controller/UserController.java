package controller;

import dao.UserDao;
import dao.impl.UserDaoImpl;
import exception.DALException;
import exception.WebServiceException;
import model.User;
import model.dto.UserTO;
import util.KeyGenerator;

import javax.ws.rs.core.Response;

public class UserController {

    private UserDao userDao;

    public UserController() throws Exception {
        try {
            userDao = new UserDaoImpl();
        } catch (Exception e) {
            throw new DALException(e.getMessage());
        }
    }

    public boolean validateApiKey(String apiKey) throws WebServiceException {
        return userDao.validateApiKey(apiKey);
    }

    public User login(String username, String password) throws WebServiceException {
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

        return new User(user.getUsername(), user.getName(), user.getApiKey(), user.isAdmin());
    }

    public Response createUser(String username, String password, String name, String adminStr, String apiKey) throws WebServiceException  {
        if (!validateApiKeyAdmin(apiKey)) {
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
        if (!validateApiKeyAdmin(apiKey)) {
            throw new WebServiceException("Authorization error: User is not authorized for this operation.");
        }

        try {
            if (username.equalsIgnoreCase("admin")) {
                throw new WebServiceException("Error: This user can't be deleted.");
            }

            userDao.deleteUser(username);
        } catch (DALException e) {
            throw new WebServiceException("Error while deleting user: " + e.getMessage());
        }

        return Response.ok().build();
    }

    public boolean validateApiKeyAdmin(String apiKey) {
        return userDao.validateApiKeyAdmin(apiKey);
    }
}
