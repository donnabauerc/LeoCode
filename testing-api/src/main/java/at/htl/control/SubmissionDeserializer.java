package at.htl.control;

import at.htl.entities.Submission;
import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;

public class SubmissionDeserializer extends ObjectMapperDeserializer<Submission> {
    public SubmissionDeserializer() {
        super(Submission.class);
    }
}
