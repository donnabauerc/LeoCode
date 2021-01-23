package at.htl.resources;

import at.htl.repositories.ExampleRepository;
import org.jboss.logging.Logger;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/example")
public class ExampleEndpoint {

    @Inject
    Logger log;

    @Inject
    ExampleRepository exampleRepository;

    @GET
    @Path("/list")
    public Response listAll(){
        log.info("ListAll()");
        return Response.ok(exampleRepository.listAll()).build();
    }
}
