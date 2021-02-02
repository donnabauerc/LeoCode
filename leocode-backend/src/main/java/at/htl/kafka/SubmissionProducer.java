package at.htl.kafka;

import at.htl.entity.Submission;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class SubmissionProducer {

    @Inject
    Logger log;

    @Inject
    @Channel("submission-input")
    Emitter<Submission> emitter;

    public void sendSubmission(Submission s) {
        log.info("sent kafka message: " + s.toString());
        emitter.send(s);
    }
}
