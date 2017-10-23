package webservice;

import controller.RouteCalculationController;
import model.Route;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Date;

@Path("route")
public class RouteResource {

    private RouteCalculationController routeCalculationController;

    public RouteResource() {
        routeCalculationController = new RouteCalculationController();
    }

    @GET
    @Produces(MediaType.APPLICATION_XML)
    public Route getRoute() {
        return routeCalculationController.calculateRoute(null, null, new Date());
    }

}
