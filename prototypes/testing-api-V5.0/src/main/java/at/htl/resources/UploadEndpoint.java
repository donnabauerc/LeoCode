package at.htl.resources;

import at.htl.control.FileHandler;
import at.htl.control.MultipartBody;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Path("upload")
public class UploadEndpoint {

    public final static String FILE_SEPARATOR = System.getProperty("file.separator");
    public final static String OS = System.getProperty("os.name").toLowerCase();
    public static List<MultipartBody> files = new LinkedList<>();
    public static List<String> currentlyUploadedFiles;
    public static List<String> codeFiles;
    public static List<String> testFiles;
    public static String pathToProject;

    @ConfigProperty(name = "project-under-test")
    String projectUnderTest;

    @POST
    @Consumes("multipart/form-data")
    public Response uploadProject(MultipartFormDataInput input) {
        //reset();
        Map<String, List<InputPart>> uploadForm = input.getFormDataMap();

        try {
            InputStream inputStream = uploadForm.get("fileName").get(0).getBody(InputStream.class, null);
            files.add(new MultipartBody(
                    uploadForm.get("file").get(0).getBodyAsString(),
                    inputStream,
                    uploadForm.get("fileType").get(0).getBodyAsString()
            ));
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileHandler.moveToRequiredDirectory(currentlyUploadedFiles);

        return Response.ok("Uploaded " + currentlyUploadedFiles).build();
    }

    private void reset(){
        files = new LinkedList<>();
        pathToProject = ".." + FILE_SEPARATOR + projectUnderTest + FILE_SEPARATOR;
        currentlyUploadedFiles = new LinkedList<>();
        codeFiles = new LinkedList<>();
        testFiles = new LinkedList<>();
        FileHandler.clearDirectory(pathToProject);
    }

}
