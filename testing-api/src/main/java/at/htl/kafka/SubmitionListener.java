package at.htl.kafka;

import at.htl.control.FileHandler;
import at.htl.entities.LeocodeStatus;
import at.htl.entities.Submition;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class SubmitionListener {

    @Inject
    Logger log;

    @Inject
    FileHandler fileHandler;

    @Inject
    SubmitionProducer submitionProducer;

    @Incoming("submition-input")
    public void listen(Submition s) {
        if (s.status.equals(LeocodeStatus.SUBMITTED)) {
            log.info("Received Message: " + s.toString());

            Runnable runnable = () -> {
                fileHandler.testProject(s.pathToZip);
                s.result = fileHandler.getResult();
                s.status = fileHandler.evaluateStatus(s.result);
                submitionProducer.sendSubmition(s);
            };
            new Thread(runnable).start();
        }
    }
}
