package at.htl.kafka;

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
    SubmitionProducer submitionProducer;

    @Incoming("submition-input")
    public void listen(Submition s) {
        log.info("Received Message: " + s.toString());
        submitionProducer.sendSubmition(s);
    }
}
