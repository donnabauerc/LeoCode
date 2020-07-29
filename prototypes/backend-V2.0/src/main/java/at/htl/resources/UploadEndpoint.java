package at.htl.resources;

import at.htl.control.FileHandler;
import at.htl.control.MultipartService;
import at.htl.entities.Example;
import at.htl.entities.File;
import at.htl.entities.FileType;
import at.htl.entities.MultipartBody;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Path("upload")
public class UploadEndpoint {

    @Inject
    @RestClient
    MultipartService service;

    public static boolean uploadIsFromStudent;
    public static Example example;
    public List<MultipartBody> files;

    @POST
    @Consumes("multipart/form-data")
    @Transactional
    public Response handleUploads(MultipartFormDataInput input) {

        Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
        uploadIsFromStudent = (uploadForm.size() < 5);

        if(!uploadIsFromStudent){
            example = new Example();

            uploadForm.forEach((k, v) -> {
                FileHandler.uploadFile(k, v);
            });

            example.persist();
            return Response.ok("Created Example" ).build();
        }else{

            files = new LinkedList<>();

            try {
                String username = uploadForm.get("username").get(0).getBodyAsString();
                String exampleId = uploadForm.get("example").get(0).getBodyAsString();
                List<InputPart> codeFiles = uploadForm.get("code");

                Example example = Example.findById(Long.parseLong(exampleId));

                File pom = example.getPom();
                InputStream pomInputStream = new ByteArrayInputStream(pom.getFile());
                files.add(new MultipartBody(pom.getName(), pomInputStream, FileType.POM.toString()));

                example.getTests().forEach(file -> {
                    InputStream testInputStream = new ByteArrayInputStream(file.getFile());
                    files.add(new MultipartBody(file.getName(), testInputStream, FileType.TEST.toString()));
                });

                for (InputPart inputPart : codeFiles) {
                    try {
                        MultivaluedMap<String, String> header = inputPart.getHeaders();
                        String name = FileHandler.getFileName(header);
                        InputStream inputStream = inputPart.getBody(InputStream.class, null);

                        files.add(new MultipartBody(name, inputStream, FileType.CODE.toString()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                sendFile();

            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return Response.ok("Ran Tests" ).build();
        }
    }

    public void sendFile() throws Exception {
        files.forEach(multipartBody -> {
            service.sendMultipartData(multipartBody);
        });
    }

}
