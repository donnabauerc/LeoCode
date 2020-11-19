package at.htl.resources;

import at.htl.control.*;
import at.htl.entities.*;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.MediaType;
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

    @Inject
    Logger log;

    public static boolean uploadIsFromStudent;
    public static Example example;
    public List<MultipartBody> files;

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.TEXT_PLAIN)
    @Transactional
    public Response handleUploads(MultipartFormDataInput input) {
        String res = "";

        Map<String, List<InputPart>> uploadForm = input.getFormDataMap();
        uploadIsFromStudent = (uploadForm.size() < 5);

        if(!uploadIsFromStudent){
            log.info("Creating Example");
            example = new Example();

            uploadForm.forEach((k, v) -> {
                FileHandler.uploadFile(k, v);
            });

            example.persist();
            res = "Created Example";
        }else{
            files = new LinkedList<>();
            try {
                String username = uploadForm.get("username").get(0).getBodyAsString();
                String exampleId = uploadForm.get("example").get(0).getBodyAsString();
                List<InputPart> codeFiles = uploadForm.get("code");

                Example example = Example.findById(Long.parseLong(exampleId));

                //search for files in db
                File pom = File.find("select f from File f where type = ?1 and example = ?2", FileType.POM, example).firstResult();
                File jenkinsfile = File.find("select f from File f where type = ?1 and example = ?2", FileType.JENKINSFILE, example).firstResult();
                List<File> tests = File.find("select f from File f where type = ?1 and example = ?2", FileType.TEST, example).list();

                try (
                        InputStream pomInputStream = new ByteArrayInputStream(pom.getFile());
                        InputStream jenkinsInputStream = new ByteArrayInputStream(jenkinsfile.getFile());
                ) {
                    files.add(new MultipartBody(pom.getName(), pomInputStream, FileType.POM.toString()));
                    files.add(new MultipartBody(jenkinsfile.getName(), jenkinsInputStream, FileType.JENKINSFILE.toString()));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                tests.forEach(test -> {
                    try (InputStream testInputStream = new ByteArrayInputStream(test.getFile())) {
                        files.add(new MultipartBody(test.getName(), testInputStream, FileType.TEST.toString()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

                for (InputPart inputPart : codeFiles) {
                    try {
                        MultivaluedMap<String, String> header = inputPart.getHeaders();
                        String name = FileHandler.getFileName(header);

                        try (InputStream inputStream = inputPart.getBody(InputStream.class, null)) {
                            files.add(new MultipartBody(name, inputStream, FileType.CODE.toString()));
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                sendFile();
                log.info("Running Tests");

                res = service.runTests();
                log.info(res);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return Response.ok(new Result(res)).build();
    }

    public void sendFile() throws Exception {
        files.forEach(multipartBody -> {
            log.info(multipartBody.fileName);
            service.sendMultipartData(multipartBody);
        });
    }

}
