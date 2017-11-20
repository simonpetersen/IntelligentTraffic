package webservice;

import controller.RouteCalculationController;
import controller.UserController;
import dao.NodeDao;
import dao.UserDao;
import dao.impl.NodeDaoImpl;
import dao.impl.UserDaoImpl;
import exception.WebServiceException;
import model.Route;
import model.dto.NodeTO;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Path("route")
public class RouteResource {

    private RouteCalculationController routeCalculationController;
    private UserController userController;
    private NodeDao nodeDao;
    private DateFormat dateFormat;

    public RouteResource() throws Exception {
        routeCalculationController = new RouteCalculationController();
        userController = new UserController();
        nodeDao = new NodeDaoImpl();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }

    @GET
    @Produces(MediaType.APPLICATION_XML)
    @Path("/{startLat}/{startLong}/{destLat}/{destLong}/{dateString}/")
    public Route getRoute(@PathParam("startLat") double startLatitude, @PathParam("startLong") double startLongitude,
                          @PathParam("destLat") double destinationLatitude, @PathParam("destLong") double destinationLongitude,
                          @PathParam("dateString") String dateString, @QueryParam("apiKey") String apiKey) throws WebServiceException {
        if (userController.validateApiKey(apiKey)) {
            NodeTO startNode = nodeDao.getNodeByCoordinates(startLatitude, startLongitude);
            NodeTO destinationNode = nodeDao.getNodeByCoordinates(destinationLatitude, destinationLongitude);
            Date date = null;
            try {
                date = dateFormat.parse(dateString);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return routeCalculationController.calculateRoute(startNode, destinationNode, date);
        }

        throw new WebServiceException("User is not authorized.");
    }

}
