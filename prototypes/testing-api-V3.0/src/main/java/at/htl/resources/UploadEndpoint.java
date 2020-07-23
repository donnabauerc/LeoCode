package at.htl.resources;

import at.htl.control.FileHandler;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logmanager.Logger;
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

    private final Logger log = Logger.getLogger(UploadEndpoint.class.getSimpleName());

    public static List<String> files;
    public static boolean filesAreForTesting = false;

    public final static String FILE_SEPARATOR = System.getProperty("file.separator");

    public String path;

    @ConfigProperty(name = "project-under-test")
    String projectUnderTest;

    @POST
    @Consumes("multipart/form-data")
    public Response uploadProject(MultipartFormDataInput input) {
        path = ".." + FILE_SEPARATOR + projectUnderTest + FILE_SEPARATOR;
        files = new LinkedList<>();
        filesAreForTesting = false;

        Map<String, List<InputPart>> uploadForm = input.getFormDataMap();

        if(uploadForm.size() == 2){
            //Deleting possible leftovers
            FileHandler.clearDirectory(path);
            filesAreForTesting = true;
        }

        uploadForm.forEach((k,v) -> {
            FileHandler.uploadFile(path, v);
        });

        FileHandler.moveToRequiredDirectory(files);

        return Response.ok("Uploaded " + files).build();
    }

}
