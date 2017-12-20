package test.controller;

import controller.UserController;
import dao.UserDao;
import dao.impl.UserDaoImpl;

import model.User;
import model.dto.UserTO;

import org.junit.*;
import util.KeyGenerator;

import java.security.Key;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class UserControllerTest {

    private UserController userController;
    private UserTO user;

    @Before
    public void setUp() throws Exception {
        userController = new UserController();
        user = new UserTO("testuser", "testpassword", null, "Test User", true);

        String admin = user.isAdmin() ? "true" : "false";
        try {
            userController.createUser(user.getUsername(), user.getPassword(), user.getName(), admin, "abcdefgh");
        } catch (Exception e) {
            e.printStackTrace();
        }

        User retrievedUser = userController.login(user.getUsername(), user.getPassword());
        user.setApiKey(retrievedUser.getApiKey());
    }

    @After
    public void tearDown() throws Exception {
        userController.deleteUser(user.getUsername(), user.getApiKey());
    }

    @Test
    public void testLogin() throws Exception {
        User loggedInUser = userController.login(user.getUsername(), user.getPassword());
        assertNotNull(loggedInUser);
    }

    @Test
    public void testValidateApiKey() throws Exception {
        boolean validation = userController.validateApiKey(user.getApiKey());
        assertTrue(validation);
    }

    @Test
    public void testValidateApiKeyAdmin() {
        boolean validation = userController.validateApiKeyAdmin(user.getApiKey());
        assertTrue(validation);
    }
}
