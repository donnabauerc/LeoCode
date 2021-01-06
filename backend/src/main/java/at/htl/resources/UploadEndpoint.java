package at.htl.resources;

import at.htl.control.FileHandler;
import at.htl.entities.*;
import at.htl.kafka.SubmissionProducer;
import at.htl.repositories.ExampleRepository;
import at.htl.repositories.LeocodeFileRepository;
import at.htl.repositories.SubmissionRepository;
import org.jboss.logging.Logger;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.*;
import java.net.URI;
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
    SubmissionRepository submissionRepository;
    @Inject
    FileHandler fileHandler;
    @Inject
    SubmissionProducer submissionProducer;

    //Upload from Teacher
    @POST
    @Path("/example")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response uploadExample(MultipartFormDataInput input) { //TODO: Input Depends on Type => pom/build.xml
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
    public Response uploadExercise(MultipartFormDataInput input, @Context UriInfo uriInfo) {
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

            Submission submission = new Submission();
            submission.author = username;
            submission.example = example;
            submissionRepository.persist(submission);

            submission.pathToZip = fileHandler.zipLeocodeFiles(submission.id, files);
            log.info("created zip: " + submission.pathToZip);

            submissionProducer.sendSubmission(submission);
            submission.status = LeocodeStatus.SUBMITTED;
            log.info("Running Tests");

            //res = Response.seeOther(URI.create("http://localhost:9090/submission.html?id=" + submission.id.toString())).build();
            res = Response.created(URI.create("http://localhost:9090/submission.html?id=" + submission.id.toString()))
                    .entity("successfully submitted with ID " + submission.id.toString()).build();
        } catch (Exception e) {
            res = Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e).build();
            e.printStackTrace();
        }
        return res;
    }

}
