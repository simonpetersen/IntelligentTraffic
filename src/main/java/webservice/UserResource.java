package webservice;

import dao.UserDao;
import dao.impl.UserDaoImpl;
import exceptions.WebServiceException;
import model.dto.UserTO;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;

@Path("user")
public class UserResource {

    private UserDao userDao;

    public UserResource() {
        try {
            userDao = new UserDaoImpl();
        } catch (Exception e) {
            // TODO: Handle exception
            e.printStackTrace();
        }
    }

    @POST
    @Path("login/{username}")
    @Produces(MediaType.APPLICATION_XML)
    public UserTO login(@PathParam("username") String username, @Context HttpHeaders httpHeaders) throws Exception {
        MultivaluedMap<String, String> headers = httpHeaders.getRequestHeaders();
        String password = headers.getFirst("password");
        if (username == null || password == null || username.isEmpty() || password.isEmpty()) {
            throw new WebServiceException("Username or password can't be empty");
        }

        UserTO user = userDao.login(username, password);

        return user;
    }

    @POST
    @Path("{username}")
    public void createUser(@PathParam("username") String username, @Context HttpHeaders httpHeaders) {

    }

    @DELETE
    public void deleteUser(@FormParam("username") String username) {

    }

}
