package at.htl.resources;

import at.htl.control.StreamGobbler;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logmanager.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.Executors;

@Path("run")
public class TestEndpoint {

    private final Logger log = Logger.getLogger(TestEndpoint.class.getSimpleName());

    @GET
    public Response testProject() throws IOException, InterruptedException {
        log.info("Run tests");

        ProcessBuilder builder = new ProcessBuilder("../run-tests.sh");
        Process process = builder.start();

        StreamGobbler streamGobbler =
                new StreamGobbler(process.getInputStream(), System.out::println);
        Executors.newSingleThreadExecutor().submit(streamGobbler);
        int exitCode = process.waitFor();
        assert exitCode == 0;

        return Response.ok("Executed Tests").build();
    }
}

