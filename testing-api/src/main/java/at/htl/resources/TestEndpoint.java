package at.htl.resources;

import at.htl.control.FileHandler;
import at.htl.control.MultipartBody;
import org.jboss.logmanager.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

@Path("run")
public class TestEndpoint {

    private final Logger log = Logger.getLogger(TestEndpoint.class.getSimpleName());

    @GET
    public Response testProject() throws IOException, InterruptedException {
        setup(UploadEndpoint.files);
        log.info("Running tests");

        ProcessBuilder builder =  new ProcessBuilder("../run-tests.sh");
        Process process = builder.start();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        }

        int exitCode = process.waitFor();
        assert exitCode == 0;

        UploadEndpoint.reset();

        return Response.ok(FileHandler.fetchResult()).build();
    }

    public void setup(List<MultipartBody> files){
        log.info("Setup Files");
        files.forEach(multipartBody -> {
            FileHandler.uploadFile(multipartBody);
        });

        FileHandler.moveToRequiredDirectory();
    }
}
