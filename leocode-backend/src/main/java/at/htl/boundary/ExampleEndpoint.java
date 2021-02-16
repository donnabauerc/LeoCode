package at.htl.boundary;

import at.htl.entity.Example;
import at.htl.repository.ExampleRepository;
import org.jboss.logging.Logger;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/example")
public class ExampleEndpoint {

    @Inject
    Logger log;

    @Inject
    ExampleRepository exampleRepository;

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createExample(MultipartFormDataInput input) {
        try {
            Example example = exampleRepository.createExampleFromMultipart(input);
            log.info("createExample("+ example.toString() +")");
            return Response.ok(example).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.ok("Something went wrong!").build();
        }
    }
}
