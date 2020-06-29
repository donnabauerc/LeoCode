package at.htl.resources;

import at.htl.control.StreamGobbler;
import org.jboss.logmanager.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.Executors;

@Path("run")
public class TestEndpoint {

    private final Logger log = Logger.getLogger(TestEndpoint.class.getSimpleName());

    @GET
    public void testProject() throws IOException, InterruptedException {
        log.info("Run tests");

        boolean isSchwammerl = System.getProperty("os.name")
                .toLowerCase().startsWith("windows");

        ProcessBuilder builder;
        if (isSchwammerl) {
            builder = new ProcessBuilder("../run-tests.cmd");
            log.info("Windows not supported yet");
        } else {
            builder = new ProcessBuilder("../run-tests.sh");
        }

        Process process = builder.start();
        StreamGobbler streamGobbler =
                new StreamGobbler(process.getInputStream(), System.out::println);
        Executors.newSingleThreadExecutor().submit(streamGobbler);
        int exitCode = process.waitFor();
        assert exitCode == 0;
    }
}

