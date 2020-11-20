package at.htl.resources;

import at.htl.control.*;
import org.jboss.logging.Logger;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

@Path("run")
public class TestEndpoint {

    @Inject
    Logger log;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Response testProject() {
        Response res;
        try {
            FileHandler.moveToRequiredDirectory();
            log.info("Running tests");

            ProcessBuilder builder =  new ProcessBuilder("../run-tests.sh");
            Process process = builder.start();

            int exitCode = process.waitFor();
            assert exitCode == 0;

            log.info("Successfully Tested Project");
            res = Response.ok("Successfully Tested Project").build();
            //res = Response.ok(FileHandler.fetchResult()).build();
        } catch (Exception e) {
            res = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e).build();
            e.printStackTrace();
        }
        return res;
    }
}
