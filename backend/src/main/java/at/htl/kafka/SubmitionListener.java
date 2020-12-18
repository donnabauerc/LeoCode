package at.htl.kafka;

import at.htl.entities.Submition;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class SubmitionListener {
    @Inject
    Logger log;

    @Incoming("submition-result")
    public void listen(Submition s) {
        //TODO: merge s
        log.info("Finished Testing: " + s.toString());
        //TODO: notify user
    }
}
