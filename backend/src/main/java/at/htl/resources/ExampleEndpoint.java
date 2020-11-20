package at.htl.resources;

import at.htl.entities.Example;
import at.htl.entities.File;
import org.jboss.logging.Logger;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/example")
public class ExampleEndpoint {

    @Inject
    Logger log;

    @GET
    @Path("/getAll")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        log.info("Received Get All Request");
        Response res = Response.ok(Example.listAll()).build();
        res.getHeaders().add("Access-Control-Allow-Origin", "*");
        return res;
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response getById(@PathParam("id") long id) {
        log.info("Received Get By Id Request");
        Example e = Example.findById(id);
        ExampleResponse exampleResponse = new ExampleResponse(e, File.find("select f from File f where example = ?1", e).list());
        Response res = Response.ok(exampleResponse).build();
        res.getHeaders().add("Access-Control-Allow-Origin", "*");
        return res;
    }
}

class ExampleResponse {
    public Example example;
    public List<File> files;

    public ExampleResponse(Example example, List<File> files) {
        this.example = example;
        this.files = files;
    }
}
