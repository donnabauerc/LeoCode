package at.htl.kafka;

import at.htl.entities.Submition;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class SubmitionProducer {

    @Inject
    Logger log;

    @Inject
    @Channel("submition-input")
    Emitter<Submition> emitter;

    public void sendSubmition(Submition s) {
        log.info("sent kafka message: " + s.toString());
        emitter.send(s);
    }
}
