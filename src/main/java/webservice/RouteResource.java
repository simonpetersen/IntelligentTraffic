package webservice;

import controller.DataController;
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
    private DateFormat dateFormat;
    private DataController dataController;

    public RouteResource() throws WebServiceException {
        try {
            routeCalculationController = new RouteCalculationController();
            dataController = new DataController();
            userController = new UserController();
            dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        } catch (Exception e) {
            throw new WebServiceException(e.getMessage());
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_XML)
    @Path("/{startLat}/{startLong}/{destLat}/{destLong}/{dateString}/")
    public Route getRoute(@PathParam("startLat") double startLatitude, @PathParam("startLong") double startLongitude,
                          @PathParam("destLat") double destinationLatitude, @PathParam("destLong") double destinationLongitude,
                          @PathParam("dateString") String dateString, @QueryParam("apiKey") String apiKey) throws WebServiceException {
        if (userController.validateApiKey(apiKey)) {
            Date date;
            try {
                date = dateFormat.parse(dateString);
            } catch (ParseException e) {
                e.printStackTrace();
                date = new Date();
            }

            try {
                return routeCalculationController.calculateRoute(startLatitude, startLongitude, destinationLatitude,
                        destinationLongitude, date);
            } catch (Exception e) {
                throw new WebServiceException(e.getMessage());
            }
        }

        throw new WebServiceException("User is not authorized.");
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/initmap/")
    public String initMap() {
        try {
            dataController.saveMapInDB();
            return "Map initialized.";
        } catch (Exception e) {
            return e.getMessage();
        }
    }

}
