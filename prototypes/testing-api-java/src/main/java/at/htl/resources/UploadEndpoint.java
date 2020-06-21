package at.htl.resources;

import at.htl.control.FileGenerator;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logmanager.Logger;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Path("/upload")
public class UploadEndpoint {

    @ConfigProperty(name = "project-under-test-folder")
    String projectUnderTestFolder;

    private final Logger log = Logger.getLogger(UploadEndpoint.class.getSimpleName());

    @POST
    @Path("/{type}")
    @Consumes("multipart/form-data")
    public Response uploadFile(MultipartFormDataInput input, @PathParam("type")String type) throws IOException {

        switch (type){
            case "code":
                projectUnderTestFolder += "main/java";
                break;
            case "test":
                projectUnderTestFolder += "test/java";
                break;
            default:
                throw new IOException("Invalid Link! => http://localhost:8080/upload/"+type);
        }

        Map<String, List<InputPart>> uploadForm = input.getFormDataMap();

        // depends on form eg. name="uploadedFile"
        List<InputPart> inputParts = uploadForm.get("uploadedFile");

        String fileName = FileGenerator.uploadFile(projectUnderTestFolder + System.getProperty("file.separator"), inputParts);
        log.info("Uploaded " + fileName);

        return Response.ok("Uploaded "+fileName).build();
    }
}
