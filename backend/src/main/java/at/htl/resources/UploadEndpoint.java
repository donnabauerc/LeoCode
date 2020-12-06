package at.htl.resources;

import at.htl.control.FileHandler;
import at.htl.entities.*;
import at.htl.repositories.ExampleRepository;
import at.htl.repositories.LeocodeFileRepository;
import at.htl.repositories.SubmitionRepository;
import org.jboss.logging.Logger;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Path("/upload")
public class UploadEndpoint {
    @Inject
    Logger log;
    @Inject
    ExampleRepository exampleRepository;
    @Inject
    LeocodeFileRepository fileRepository;
    @Inject
    SubmitionRepository submitionRepository;
    @Inject
    FileHandler fileHandler;

    //Upload from Teacher
    @POST
    @Path("/example")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response uploadExample(MultipartFormDataInput input) {
        log.info("Received UploadExample Request");
        Example example = exampleRepository.createExampleFromMultipartFiles(input);
        return Response.ok(example).build();
    }

    //Upload from Student
    @POST
    @Path("/exercise")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response uploadExercise(MultipartFormDataInput input) {
        log.info("Received UploadExercise Request");

        Response res;
        List<LeocodeFile> files = new LinkedList<>();
        Map<String, List<InputPart>> uploadForm = input.getFormDataMap();

        try {
            String username = uploadForm.get("username").get(0).getBodyAsString();
            String exampleId = uploadForm.get("example").get(0).getBodyAsString();

            List<InputPart> codeFiles = uploadForm.get("code");
            Example example = exampleRepository.findById(Long.parseLong(exampleId));

            //search for files in db
            files.addAll(fileRepository.getFilesRequiredForTesting(example));
            //add code from student
            files.addAll(fileRepository.persistFilesFromMultipart(LeocodeFileType.CODE.toString(), username, codeFiles, example));

            String location = fileHandler.zipLeocodeFiles(files);
            log.info("created zip: " + location);

            Submition submition = new Submition(location, LeocodeStatus.CREATED, username);
            submitionRepository.persist(submition);

            //sendFile();

            log.info("Running Tests");
            //String testResult = service.runTests();

            //log.info(testResult);
            //res = Response.ok(testResult).build();
            res = Response.ok().build();
        } catch (Exception e) {
            res = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e).build();
            e.printStackTrace();
        }

        return Response.ok(res).build();
    }

}
