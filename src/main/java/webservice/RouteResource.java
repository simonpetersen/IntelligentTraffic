package webservice;

import controller.RouteCalculationController;
import dao.UserDao;
import dao.impl.UserDaoImpl;
import exception.WebServiceException;
import model.Route;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Date;

@Path("route")
public class RouteResource {

    private RouteCalculationController routeCalculationController;
    private UserDao userDao;

    public RouteResource() throws Exception {
        routeCalculationController = new RouteCalculationController();
        userDao = new UserDaoImpl();
    }

    @GET
    @Produces(MediaType.APPLICATION_XML)
    @Path("/{startLat}/{startLong}/{destLat}/{destLong}/{date}/")
    public Route getRoute(@PathParam("startLat") double startLatitude, @PathParam("startLong") double startLongitude,
                          @PathParam("destLat") double destinationLatitude, @PathParam("destLong") double destinationLongitude,
                          @PathParam("date") Date date, @QueryParam("apiKey") String apiKey) throws WebServiceException {
        if (userDao.validateApiKey(apiKey)) {
            return routeCalculationController.calculateRoute(null, null, new Date());
        }

        throw new WebServiceException("ApiKey is not valid.");
    }

}
