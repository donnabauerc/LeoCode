package at.htl.resources;

import at.htl.repositories.SubmissionRepository;
import org.jboss.logging.Logger;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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
        return Response.ok(submissionRepository.getFinished(username)).build();
    }
}
