package at.htl.boundary;

import at.htl.entity.Example;
import at.htl.repository.ExampleRepository;
import org.jboss.logging.Logger;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
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
        Example example = exampleRepository.createExampleFromMultipart(input);
        log.info("createExample("+ example.toString() +")");
        if(example == null) {
            return Response.ok("Something went wrong!").build();
        } else {
            return Response.ok(example).build();
        }
    }
}
