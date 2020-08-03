package at.htl.resources;

import at.htl.control.FileHandler;
import at.htl.control.MultipartBody;
import org.jboss.logmanager.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.List;

@Path("run")
public class TestEndpoint {

    private final Logger log = Logger.getLogger(TestEndpoint.class.getSimpleName());

    @GET
    public Response testProject() throws IOException, InterruptedException {
        setup(UploadEndpoint.files);
        log.info("Running tests");

        ProcessBuilder builder;

//        if(!UploadEndpoint.OS.contains("win")){
//            builder = new ProcessBuilder("../run-tests.sh");
//        }else{
//            builder = new ProcessBuilder("..\\run-tests.bat");
//        }
//        Process process = builder.start();
//
//        int exitCode = process.waitFor();
//        assert exitCode == 0;

        //reset
        return Response.ok("Executed Tests").build();
    }

    public void setup(List<MultipartBody> files){
        log.info("Setup Files");
        files.forEach(multipartBody -> {
            FileHandler.uploadFile(multipartBody);
        });

        FileHandler.moveToRequiredDirectory();
    }
}
