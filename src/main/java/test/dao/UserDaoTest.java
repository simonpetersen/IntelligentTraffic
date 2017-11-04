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
    public void testKeyGeneratoin() throws Exception {
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
    public void testCreateUserWithNonUniqueKey() throws Exception {
        try {
            UserTO user = new UserTO("test1", null, "abcd", "Test User", false);
            userDao.createUser(user);

            user.setUsername("test2");
            userDao.createUser(user);

            // Delete rows
            userDao.deleteUser("test1");
            userDao.deleteUser("test2");
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }
}
