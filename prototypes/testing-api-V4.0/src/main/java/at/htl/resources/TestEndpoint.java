package at.htl.resources;

import org.jboss.logmanager.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.io.IOException;

@Path("run")
public class TestEndpoint {

    private final Logger log = Logger.getLogger(TestEndpoint.class.getSimpleName());

    @GET
    public Response testProject() throws IOException, InterruptedException {
        log.info("Running tests");

        ProcessBuilder builder;

        if(!UploadEndpoint.OS.contains("win")){
            builder = new ProcessBuilder("../run-tests.sh");
        }else{
            builder = new ProcessBuilder("..\\run-tests.bat");
        }
        Process process = builder.start();

        int exitCode = process.waitFor();
        assert exitCode == 0;

        return Response.ok("Executed Tests").build();
    }
}
