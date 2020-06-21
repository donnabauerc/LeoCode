package at.htl.resources;


import org.jboss.logmanager.Logger;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Map;

@Path("/test")
public class TestEndpoint {

    private final Logger log = Logger.getLogger(TestEndpoint.class.getSimpleName());

    @POST
    @Path("/upload")
    @Consumes("multipart/form-data")
    public Response uploadFile(MultipartFormDataInput input) {
        Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
        // depends on form eg. name="uploadedFile"
        List<InputPart> inputParts = uploadForm.get("uploadedFile");

        String fileName = FileGenerator.uploadFile("../src/test/java/at/htl/examples/", inputParts);
        log.info("Uploaded " + fileName);

        return Response.ok("Uploaded "+fileName).build();
    }


}