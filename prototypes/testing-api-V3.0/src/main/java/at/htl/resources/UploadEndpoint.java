package at.htl.resources;

import at.htl.control.FileHandler;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Path("upload")
public class UploadEndpoint {

    public final static String FILE_SEPARATOR = System.getProperty("file.separator");
    public static List<String> currentlyUploadedFiles;
    public static boolean filesAreForTesting = false;
    private String pathToProject;

    @ConfigProperty(name = "project-under-test")
    String projectUnderTest;

    public UploadEndpoint() {

    }

    @POST
    @Consumes("multipart/form-data")
    public Response uploadProject(MultipartFormDataInput input) {

        reset();

        Map<String, List<InputPart>> uploadForm = input.getFormDataMap();

        if (uploadForm.size() == 2) {
            FileHandler.clearDirectory(pathToProject);
            filesAreForTesting = true;
        }

        uploadForm.forEach((k, v) -> {
            FileHandler.uploadFile(pathToProject, v);
        });

        FileHandler.moveToRequiredDirectory(currentlyUploadedFiles);

        return Response.ok("Uploaded " + currentlyUploadedFiles).build();
    }

    private void reset(){
        pathToProject = ".." + FILE_SEPARATOR + projectUnderTest + FILE_SEPARATOR;
        currentlyUploadedFiles = new LinkedList<>();
        filesAreForTesting = false;
    }

}
