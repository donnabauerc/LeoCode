package at.htl.resources;

import at.htl.entities.Submission;
import at.htl.repositories.SubmissionRepository;
import org.jboss.logging.Logger;

import javax.inject.Inject;
import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Path("/portfolio")
public class PortfolioEndpoint {

    @Inject
    Logger log;

    @Inject
    SubmissionRepository submissionRepository;

    @GET
    @Path("/{username}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getFinishedSubmitions(@PathParam("username") String username){
        log.info("Received getFinishedSubmitions(" + username + ") Request");
        List<Submission> finishedSubmissions = submissionRepository.getFinished(username);
        JsonArrayBuilder res = Json.createArrayBuilder();
        finishedSubmissions.forEach(s -> {
            res.add(Json.createObjectBuilder()
                    .add("id", s.id)
                    .add("status", s.getStatus().toString())
                    .add("result", s.result)
                    .add("lastTimeChanged", s.lastTimeChanged
                            .format(DateTimeFormatter.ofPattern("HH:mm dd-MM-yyyy")))
                    .build()
            );
        });

        return Response.ok(res.build()).build();
    }
}
