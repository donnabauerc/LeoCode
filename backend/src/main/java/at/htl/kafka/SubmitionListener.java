package at.htl.kafka;

import at.htl.entities.Submition;
import at.htl.repositories.SubmitionRepository;
import org.eclipse.microprofile.context.ManagedExecutor;
import org.eclipse.microprofile.context.ThreadContext;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class SubmitionListener {

    @Inject
    Logger log;

    @Inject
    SubmitionRepository submitionRepository;

    @Incoming("submition-result")
    public void listen(Submition s) {
        //https://stackoverflow.com/questions/58534957/how-to-execute-jpa-entity-manager-operations-inside-quarkus-kafka-consumer-metho
        ManagedExecutor executor = ManagedExecutor.builder()
                .maxAsync(5)
                .propagated(ThreadContext.CDI,
                        ThreadContext.TRANSACTION)
                .build();
        ThreadContext threadContext = ThreadContext.builder()
                .propagated(ThreadContext.CDI,
                        ThreadContext.TRANSACTION)
                .build();

        executor.runAsync(threadContext.contextualRunnable(() -> {
            try {
                submitionRepository.update(s);
                log.info("Finished Testing: " + s.toString());
            } catch(Exception e) {
                log.error("Something wrong happened !!!", e);
            }
        }));
    }
}
