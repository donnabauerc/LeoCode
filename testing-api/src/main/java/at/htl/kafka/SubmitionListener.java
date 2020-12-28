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
        log.info("Received Message: " + s.toString());

        Runnable runnable = () -> {
            s.status = LeocodeStatus.SUCCESS; //just for now, depends on Test Result
            s.result = fileHandler.testProject(s.pathToZip);
            submitionProducer.sendSubmition(s);
        };
        new Thread(runnable).start();
    }
}
