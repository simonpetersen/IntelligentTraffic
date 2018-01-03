package webservice;

import controller.UserController;
import dao.UserDao;
import dao.impl.UserDaoImpl;
import exception.DALException;
import exception.WebServiceException;
import model.User;
import model.dto.UserTO;
import util.KeyGenerator;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.sql.SQLIntegrityConstraintViolationException;

@Path("user")
public class UserResource {

    private UserController userController;

    public UserResource() throws WebServiceException {
        try {
            userController = new UserController();
        } catch (Exception e) {
            throw new WebServiceException(e.getMessage());
        }
    }

    @POST
    @Path("login/{username}")
    @Produces(MediaType.APPLICATION_XML)
    public User login(@PathParam("username") String username, @Context HttpHeaders httpHeaders) throws Exception {
        MultivaluedMap<String, String> headers = httpHeaders.getRequestHeaders();
        String password = headers.getFirst("password");

        return userController.login(username, password);
    }

    @POST
    @Path("{username}")
    public Response createUser(@PathParam("username") String username, @QueryParam("apiKey") String apiKey,
                               @Context HttpHeaders httpHeaders) throws Exception {
        MultivaluedMap<String, String> headers = httpHeaders.getRequestHeaders();
        String password = headers.getFirst("password");
        String name = headers.getFirst("name");
        String adminStr = headers.getFirst("admin");

        return userController.createUser(username, password, name, adminStr, apiKey);
    }

    @DELETE
    @Path("{username}")
    public Response deleteUser(@PathParam("username") String username, @QueryParam("apiKey") String apiKey) throws Exception {
        return userController.deleteUser(username, apiKey);
    }

    @POST
    @Path("changepass/{username}")
    public Response changePassword(@PathParam("username") String username, @QueryParam("apiKey") String apiKey, @Context HttpHeaders httpHeaders) throws Exception {
        MultivaluedMap<String, String> headers = httpHeaders.getRequestHeaders();
        String password = headers.getFirst("password");
        return userController.changePassword(username, apiKey, password);
    }
}
