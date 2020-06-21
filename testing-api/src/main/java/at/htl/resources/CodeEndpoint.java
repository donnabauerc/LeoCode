
package at.htl.resources;

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

@Path("/code")
public class CodeEndpoint {

    @ConfigProperty(name = "project-under-test-folder")
    String projectUnderTestFolder;

    private final Logger log = Logger.getLogger(CodeEndpoint.class.getSimpleName());

    @POST
    @Path("/upload")
    @Consumes("multipart/form-data")
    public Response uploadFile(MultipartFormDataInput input) {

        Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
        // depends on form eg. name="uploadedFile"
        List<InputPart> inputParts = uploadForm.get("uploadedFile");

        String fileName = FileGenerator.uploadFile(projectUnderTestFolder + System.getProperty("file.separator"), inputParts);
        log.info("Uploaded " + fileName);

        return Response.ok("Uploaded "+fileName).build();
    }
}