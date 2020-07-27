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
    public final static String OS = System.getProperty("os.name").toLowerCase();
    public static boolean filesAreForTesting = false;


    @POST
    @Consumes("multipart/form-data")
    public Response uploadProject(MultipartFormDataInput input) {

        Map<String, List<InputPart>> uploadForm = input.getFormDataMap();

        filesAreForTesting = (uploadForm.size() == 2);

        uploadForm.forEach((k, v) -> {
            FileHandler.uploadFile(v);
        });

        return Response.ok("Uploaded" ).build();
    }

}
