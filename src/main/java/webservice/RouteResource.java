package webservice;

import controller.RouteCalculationController;
import dao.NodeDao;
import dao.UserDao;
import dao.impl.NodeDaoImpl;
import dao.impl.UserDaoImpl;
import exception.WebServiceException;
import model.Route;
import model.dto.NodeTO;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.Date;

@Path("route")
public class RouteResource {

    private RouteCalculationController routeCalculationController;
    private UserDao userDao;
    private NodeDao nodeDao;

    public RouteResource() throws Exception {
        routeCalculationController = new RouteCalculationController();
        userDao = new UserDaoImpl();
        nodeDao = new NodeDaoImpl();
    }

    @GET
    @Produces(MediaType.APPLICATION_XML)
    //@Path("/{startLat}/{startLong}/{destLat}/{destLong}/{date}/")
    @Path("/{startLat}/{startLong}/{destLat}/{destLong}/")
    public Route getRoute(@PathParam("startLat") double startLatitude, @PathParam("startLong") double startLongitude,
                          @PathParam("destLat") double destinationLatitude, @PathParam("destLong") double destinationLongitude,
                          @QueryParam("apiKey") String apiKey) throws WebServiceException {
        if (userDao.validateApiKey(apiKey)) {
            NodeTO startNode = nodeDao.getNodeByCoordinates(startLatitude, startLongitude);
            NodeTO destinationNode = nodeDao.getNodeByCoordinates(destinationLatitude, destinationLongitude);
            Date date = new Date();

            return routeCalculationController.calculateRoute(startNode, destinationNode, date);
        }

        throw new WebServiceException("User is not authorized.");
    }

}
