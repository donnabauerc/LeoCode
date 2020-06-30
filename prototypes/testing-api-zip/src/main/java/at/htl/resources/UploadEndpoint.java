package at.htl.resources;

import at.htl.control.FileGenerator;
import net.lingala.zip4j.ZipFile;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logmanager.Logger;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Path("/upload")
public class UploadEndpoint {

    @ConfigProperty(name = "project-under-test-folder")
    String projectUnderTestFolder;

    private final Logger log = Logger.getLogger(UploadEndpoint.class.getSimpleName());

    @POST
    @Path("/project")
    @Consumes("multipart/form-data")
    public Response uploadFile(MultipartFormDataInput input) throws IOException {

        Map<String, List<InputPart>> uploadForm = input.getFormDataMap();

        // depends on form eg. name="uploadedFile"
        List<InputPart> inputParts = uploadForm.get("uploadedFile");

        String fileName = FileGenerator.uploadFile(projectUnderTestFolder + System.getProperty("file.separator"), inputParts);
        log.info("Uploaded " + fileName);

        try {
            log.info("Unzip " + fileName);
            ZipFile zipFile = new ZipFile(fileName);
            zipFile.extractAll(projectUnderTestFolder);
        } catch (Exception e) {
            e.printStackTrace();
        }

        log.info("Delete " + fileName);
        new File(fileName).delete();

        return Response.ok("Uploaded "+fileName).build();
    }
}
