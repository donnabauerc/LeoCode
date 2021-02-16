package at.htl.boundary;

import at.htl.dto.SubmissionDTO;
import at.htl.entity.Example;
import at.htl.entity.LeocodeFile;
import at.htl.entity.Submission;
import at.htl.entity.SubmissionStatus;
import at.htl.kafka.SubmissionProducer;
import at.htl.repository.ExampleRepository;
import at.htl.repository.LeocodeFileRepository;
import at.htl.repository.SubmissionRepository;
import org.jboss.logging.Logger;
import org.jboss.resteasy.plugins.providers.multipart.InputPart;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

@Path("/submission")
public class SubmissionEndpoint {

    @Inject
    Logger log;

    @Inject
    ExampleRepository exampleRepository;

    @Inject
    LeocodeFileRepository leocodeFileRepository;

    @Inject
    SubmissionRepository submissionRepository;

    @Inject
    SubmissionProducer submissionProducer;

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Response createSubmission(MultipartFormDataInput input){
        Response res;
        List<LeocodeFile> files = new LinkedList<>();
        Map<String, List<InputPart>> uploadForm = input.getFormDataMap();

        try {
            String username = uploadForm.get("username").get(0).getBodyAsString();
            String exampleId = uploadForm.get("example").get(0).getBodyAsString();
            List<InputPart> codeFiles = uploadForm.get("code");

            Example example = exampleRepository.findById(Long.parseLong(exampleId));
            if(username.isEmpty() || example == null || codeFiles.isEmpty()) {
                return Response.ok("Something went wrong!").build();
            }
            //search for files in db
            files.addAll(leocodeFileRepository.getFilesRequiredForTesting(example));
            //add code from student
            files.addAll(leocodeFileRepository.createFilesFromInputParts("code", codeFiles, username, example));

            Submission submission = new Submission();
            submission.author = username;
            submission.example = example;
            submissionRepository.persist(submission);

            submission.pathToProject = leocodeFileRepository.zipLeocodeFiles(submission.id, files);

            if(submission.pathToProject == null) {
                return Response.ok("Something went wrong!").build();
            }

            submissionProducer.sendSubmission(submission);
            submission.setStatus(SubmissionStatus.SUBMITTED);

            log.info("createSubmission(" + submission + ")");
            log.info("Running Tests");

            return Response.ok(submission.id.toString()).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(e).build();
        }
    }

    @GET
    @Path("/history/{username}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response listFinishedSubmissions(@PathParam("username") String username){
        List<Submission> submissions = submissionRepository.getFinishedByUsername(username);
        return Response.ok(submissionRepository.createSubmissionDTOs(submissions)).build();
    }


}
