package at.htl.kafka;

import at.htl.entity.Submission;
import at.htl.entity.SubmissionStatus;
import at.htl.repository.SubmissionRepository;
import org.eclipse.microprofile.context.ManagedExecutor;
import org.eclipse.microprofile.context.ThreadContext;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class SubmissionListener {

    @Inject
    Logger log;

    @Inject
    SubmissionRepository submissionRepository;

    @Incoming("submission-result") // add @Blocking Annotation?
    public void listen(Submission s) {
        if (!s.getStatus().equals(SubmissionStatus.SUBMITTED)) {
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
                    submissionRepository.update(s);
                    log.info("Finished Testing: " + s.toString());
                } catch (Exception e) {
                    log.error("Something wrong happened", e);
                }
            }));
        }
    }
}
