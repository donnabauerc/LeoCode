package at.htl.resources;

import at.htl.entities.Example;
import org.jboss.logmanager.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.awt.*;

@Path("/example")
public class ExampleEndpoint {

    private static final Logger log = Logger.getLogger(ExampleEndpoint.class.getSimpleName());

    @GET
    @Path("/getAll")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        log.info("Received Get All Request");
        return Response.ok(Example.listAll()).build();
    }
}
