package at.htl.resources;

import at.htl.control.FileHandler;
import at.htl.control.MultipartBody;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Path("upload")
public class UploadEndpoint {

    public final static String FILE_SEPARATOR = System.getProperty("file.separator");
    public final static String OS = System.getProperty("os.name").toLowerCase();
    public static List<MultipartBody> files = new LinkedList<MultipartBody>();
    public static String pathToProject;
    @Inject
    Logger log;
    @ConfigProperty(name = "project-under-test")
    String projectUnderTest;

    @POST
    @Consumes("multipart/form-data")
    public Response uploadProject(MultipartFormDataInput input) {
        Response res;
        pathToProject = ".." + FILE_SEPARATOR + projectUnderTest + FILE_SEPARATOR;
        FileHandler.currentFiles = new HashMap<>();

        try {
            FileHandler.setup();
            log.info("Received Project");

            Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
            MultipartBody multipartBody = new MultipartBody(uploadForm.get("fileName").get(0).getBodyAsString(),
                    uploadForm.get("file").get(0).getBody(InputStream.class, null));

            FileHandler.unzipProject(multipartBody);

            res = Response.ok("Uploaded File").build();
        } catch (Exception e) {
            res = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e).build();
            e.printStackTrace();
        }
        return res;
    }
}
