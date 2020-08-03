package at.htl.control;


import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/run")
@RegisterRestClient
public interface RunningTestService {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    String runTests();
}
