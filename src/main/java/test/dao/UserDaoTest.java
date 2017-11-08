package test.dao;

import dao.UserDao;
import dao.impl.UserDaoImpl;

import model.dto.UserTO;

import org.junit.Before;
import org.junit.Test;
import util.KeyGenerator;

import java.security.Key;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class UserDaoTest {

    private UserDao userDao;

    @Before
    public void setUp() throws Exception {
        userDao = new UserDaoImpl();
    }

    @Test
    public void testLogin() throws Exception {
        String username = "sipe";
        String password = "simonpetersen";

        UserTO user = userDao.login(username, password);

        assertNotNull(user);
    }

    @Test
    public void testKeyGeneration() throws Exception {
        List<String> generatedKeys = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            String randomKey = KeyGenerator.generateApiKey();
            System.out.println(randomKey.length() + " : " + randomKey);
            if (generatedKeys.contains(randomKey)) {
                fail();
            } else {
                generatedKeys.add(randomKey);
            }
        }
    }

    @Test
    public void testValidateApiKeyAdmin() throws Exception {
        String key = "a1414c8b-70d6-4b6e-aabf-1a5877004087";
        boolean validation = userDao.validateApiKeyAdmin(key);
        assertTrue(validation);
    }

    @Test
    public void testValidateApiKey() throws Exception {
        String key = "a1414c8b-70d6-4b6e-aabf-1a5877004087";
        boolean validation = userDao.validateApiKey(key);
        assertTrue(validation);
    }
}
