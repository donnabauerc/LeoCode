package at.htl.resources;

import at.htl.control.FileHandler;
import at.htl.entities.Example;
import at.htl.entities.File;
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
    public Response uploadExample(MultipartFormDataInput input) {

        Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
        uploadIsFromStudent = (uploadForm.size() < 5);

        if(!uploadIsFromStudent){
            example = new Example();

            uploadForm.forEach((k, v) -> {
                FileHandler.uploadFile(k, v);
            });

            example.persist();
        }else{

            try {
                String username = uploadForm.get("username").get(0).getBodyAsString();
                String exampleId = uploadForm.get("example").get(0).getBodyAsString();
                List<InputPart> files = uploadForm.get("test");

                System.out.println(username);
                System.out.println(exampleId);
                System.out.println(files);

                //callTestApi
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return Response.ok("Uploaded" ).build();
    }

}
