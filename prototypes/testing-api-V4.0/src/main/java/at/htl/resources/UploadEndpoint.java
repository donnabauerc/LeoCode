package at.htl.resources;

import at.htl.control.FileHandler;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.Response;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Path("upload")
public class UploadEndpoint {

    public final static String FILE_SEPARATOR = System.getProperty("file.separator");
    public final static String OS = System.getProperty("os.name").toLowerCase();
    public static List<String> currentlyUploadedFiles;
    public static List<String> codeFiles;
    public static List<String> testFiles;
    public static String pathToProject;

    @ConfigProperty(name = "project-under-test")
    String projectUnderTest;

    @POST
    @Consumes("multipart/form-data")
    public Response uploadProject(MultipartFormDataInput input) {
        reset();

        Map<String, List<InputPart>> uploadForm = input.getFormDataMap();

        uploadForm.forEach((k, v) -> {
            FileHandler.uploadFile(k, v);
        });

        FileHandler.moveToRequiredDirectory(currentlyUploadedFiles);

        return Response.ok("Uploaded " + currentlyUploadedFiles).build();
    }

    private void reset(){
        pathToProject = ".." + FILE_SEPARATOR + projectUnderTest + FILE_SEPARATOR;
        currentlyUploadedFiles = new LinkedList<>();
        codeFiles = new LinkedList<>();
        testFiles = new LinkedList<>();
        FileHandler.clearDirectory(pathToProject);
    }

}
