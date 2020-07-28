package at.htl.resources;

import at.htl.control.FileHandler;
import at.htl.entities.Example;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

@Path("upload")
public class UploadEndpoint {

    public static boolean uploadIsFromStudent;
    public static Example example;


    @POST
    @Consumes("multipart/form-data")
    @Transactional
    public Response uploadProject(MultipartFormDataInput input) {

        Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
        uploadIsFromStudent = (uploadForm.size() < 5);
        example = new Example();

        uploadForm.forEach((k, v) -> {
            FileHandler.uploadFile(k, v);
        });

        example.persist();

        return Response.ok("Uploaded" ).build();
    }

}
