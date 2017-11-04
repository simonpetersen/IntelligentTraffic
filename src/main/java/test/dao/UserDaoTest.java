package test.dao;

import dao.UserDao;
import dao.impl.UserDaoImpl;

import model.dto.UserTO;

import org.junit.Before;
import org.junit.Test;

import java.text.SimpleDateFormat;

import static org.junit.Assert.assertNotNull;

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
}
