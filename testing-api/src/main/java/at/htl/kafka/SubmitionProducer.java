package at.htl.kafka;

import at.htl.entities.LeocodeStatus;
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
    @Channel("submition-result")
    Emitter<Submition> emitter;

    public void sendSubmition(Submition s) {
        Runnable runnable = () -> {
            try {
                Thread.sleep(5000);
                s.status = LeocodeStatus.SUCCESS;
                s.result = "This is a TestResult ----- Build Success -----";
                emitter.send(s);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        };
        new Thread(runnable).start();
        log.info("sent kafka message: " + s.toString());
    }
}
