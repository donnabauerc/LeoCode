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
import java.util.List;
import java.util.Map;

@Path("upload")
public class UploadEndpoint {

    private final Logger log = Logger.getLogger(UploadEndpoint.class.getSimpleName());

    public final String fileSeparator = System.getProperty("file.separator");

    public String path = "..";

    @ConfigProperty(name = "project-under-test")
    String projectUnderTest;

    @POST
    @Consumes("multipart/form-data")
    public Response uploadProject(MultipartFormDataInput input) {
        path += fileSeparator + projectUnderTest + fileSeparator;
        Map<String, List<InputPart>> uploadForm = input.getFormDataMap();

        //Deleting possible leftovers
        FileHandler.clearDirectory(path);

        uploadForm.forEach((k,v) -> {
            FileHandler.uploadFile(path, v);
        });

        return Response.ok("Uploaded").build();
    }

}
