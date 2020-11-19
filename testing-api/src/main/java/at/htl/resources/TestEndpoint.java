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
import java.util.List;

@Path("run")
public class TestEndpoint {

    @Inject
    Logger log;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Response testProject() throws IOException, InterruptedException {

        setupFiles(UploadEndpoint.files);
        log.info("Running tests");

        ProcessBuilder builder =  new ProcessBuilder("../run-tests.sh");
        Process process = builder.start();

        int exitCode = process.waitFor();
        assert exitCode == 0;

        UploadEndpoint.reset();

        log.info("Successfully Tested Project");

        //return Response.ok(FileHandler.fetchResult()).build();
        return Response.ok("Successfully Tested Project").build();
    }

    public void setupFiles(List<MultipartBody> files){
        files.forEach(FileHandler::uploadFile);
        FileHandler.moveToRequiredDirectory();
    }
}
