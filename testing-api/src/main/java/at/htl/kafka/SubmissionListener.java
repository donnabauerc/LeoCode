package at.htl.kafka;

import at.htl.control.FileHandler;
import at.htl.entities.SubmissionStatus;
import at.htl.entities.Submission;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class SubmissionListener {

    @Inject
    Logger log;

    @Inject
    FileHandler fileHandler;

    @Inject
    SubmissionProducer submissionProducer;

    @Incoming("submission-input")
    public void listen(Submission s) {
        if (s.getStatus().equals(SubmissionStatus.SUBMITTED)) {
            log.info("Received Message: " + s.toString());
            Runnable runnable = () -> {
                fileHandler.testProject(s.pathToProject, s.example.type);
                s.result = fileHandler.getResult();
                s.setStatus(fileHandler.evaluateStatus(s.result));
                submissionProducer.sendSubmition(s);
            };
            new Thread(runnable).start();
        }
    }
}
