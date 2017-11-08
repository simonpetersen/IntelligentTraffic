package webservice;

import dao.UserDao;
import dao.impl.UserDaoImpl;
import exception.DALException;
import exception.WebServiceException;
import model.dto.UserTO;
import util.KeyGenerator;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.sql.SQLIntegrityConstraintViolationException;

@Path("user")
public class UserResource {

    private UserDao userDao;

    public UserResource() throws WebServiceException {
        try {
            userDao = new UserDaoImpl();
        } catch (Exception e) {
            throw new WebServiceException(e.getMessage());
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
    public Response createUser(@PathParam("username") String username, @QueryParam("apiKey") String apiKey,
                               @Context HttpHeaders httpHeaders) throws Exception {
        if (!userDao.validateApiKeyAdmin(apiKey)) {
            throw new WebServiceException("Authorization error: User is not authorized for this operation.");
        }

        MultivaluedMap<String, String> headers = httpHeaders.getRequestHeaders();
        String password = headers.getFirst("password");
        String name = headers.getFirst("name");
        String adminStr = headers.getFirst("admin");

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

    @DELETE
    public Response deleteUser(@FormParam("username") String username, @QueryParam("apiKey") String apiKey) throws Exception {
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
