package at.htl.resources;

import at.htl.control.FileHandler;
import at.htl.control.MultipartBody;
import org.apache.commons.io.FileUtils;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logmanager.Logger;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Path("upload")
public class UploadEndpoint {

    private static final Logger log = Logger.getLogger(UploadEndpoint.class.getSimpleName());

    public final static String FILE_SEPARATOR = System.getProperty("file.separator");
    public final static String OS = System.getProperty("os.name").toLowerCase();
    public static List<MultipartBody> files = new LinkedList<>();
    public static String pathToProject;

    @ConfigProperty(name = "project-under-test")
    String projectUnderTest;

    @POST
    @Consumes("multipart/form-data")
    public Response uploadProject(MultipartFormDataInput input) {

        pathToProject = ".." + FILE_SEPARATOR + projectUnderTest + FILE_SEPARATOR;


        FileHandler.createDir();


        Map<String, List<InputPart>> uploadForm = input.getFormDataMap();

        try {
            files.add(new MultipartBody(
                    uploadForm.get("fileName").get(0).getBodyAsString(),
                    uploadForm.get("file").get(0).getBody(InputStream.class, null),
                    uploadForm.get("fileType").get(0).getBodyAsString()
            ));

            log.info("Uploaded file: " + uploadForm.get("fileName").get(0).getBodyAsString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return Response.ok("Uploaded File").build();
    }

    public static void reset(){
        files = new LinkedList<>();
        FileHandler.testFiles = new LinkedList<>();
        FileHandler.codeFiles = new LinkedList<>();
        FileHandler.currentlyUploadedFiles = new LinkedList<>();
        try {
            FileUtils.deleteDirectory(new File(pathToProject));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
