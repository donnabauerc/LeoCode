package at.htl.resources;

import at.htl.entities.LeocodeStatus;
import at.htl.entities.Submission;
import at.htl.repositories.SubmissionRepository;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.groups.MultiSubscribe;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.jboss.logging.Logger;
import org.jboss.resteasy.annotations.SseElementType;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.sse.Sse;
import javax.ws.rs.sse.SseEventSink;

@Path("/submission")
public class SubmissionEndpoint {

    @Inject
    @Channel("submission-result")
    Multi<Submission> results;

    @Inject
    SubmissionRepository submissionRepository;

    @Inject
    Logger log;

    @GET
    @Path("/{id}")
    @Produces(MediaType.SERVER_SENT_EVENTS)
    @SseElementType("text/plain")
    public void stream(@Context Sse sse, @Context SseEventSink sseEventSink, @PathParam("id") Long id) {
        Submission currentSubmission = submissionRepository.findById(id);
        boolean canSubscribe = false;

        // When someone refreshes or connects later on send them the current status
        if (currentSubmission != null) {

            String res = String.format("%tT Uhr: %s",
                            currentSubmission.lastTimeChanged,
                            currentSubmission.getStatus().toString());

            sseEventSink.send(sse.newEvent(res));
            // anything other than SUBMITTED is complete
            canSubscribe = currentSubmission.getStatus() == LeocodeStatus.SUBMITTED;
        }

        // Only allow sse if the submition is not complete
        if (canSubscribe) {
            log.info("subscribed to Submission SSE");
            MultiSubscribe<Submission> subscribe = results.subscribe();

            subscribe.with(submition -> {
                if (id.equals(submition.id)) {

                    String res = String.format("%tT Uhr: %s",
                            submition.lastTimeChanged,
                            submition.getStatus().toString());

                    sseEventSink.send(sse.newEvent(res));

                    if (submition.getStatus() != LeocodeStatus.SUBMITTED) {
                        log.info("closed SSE");
                        sseEventSink.close();
                    }
                }
            });
        }
    }
}
